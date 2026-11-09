import {
  createSlice,
  createAsyncThunk,
  createSelector,
  type PayloadAction,
} from '@reduxjs/toolkit';
import type {
  Product,
  ProductDto,
  FilterState,
} from '../types/product.types';
import type { RootState } from '../../../store/rootReducer';
import { productMapper } from '../mapper/productMapper';
import { productService } from '../service/productService';

// ── State ──

interface ProductListState {
  /** Raw DTOs cached once; re-mapped per language via selector */
  rawDtos: ProductDto[];
  loading: boolean;
  error: string | null;
  initialized: boolean;
}

const initialState: ProductListState = {
  rawDtos: [],
  loading: false,
  error: null,
  initialized: false,
};

// ── Async Thunks ──

/**
 * Fetches all products once. Raw DTOs are cached in the slice
 * and re-mapped to localized Product[] whenever lang changes
 * (via the selectMappedProducts selector).
 *
 * Mirrors Angular ProductService.getProducts() + language re-map flow.
 */
export const fetchProducts = createAsyncThunk<
  ProductDto[],
  void,
  { rejectValue: string }
>('products/fetchAll', async (_, { rejectWithValue }) => {
  try {
    return await productService.getAllProducts();
  } catch (err: any) {
    console.error('[productListSlice] Failed to fetch products:', err);
    return rejectWithValue(err?.message ?? 'Failed to fetch products');
  }
});

// ── Slice ──

const productListSlice = createSlice({
  name: 'productList',
  initialState,
  reducers: {
    /** Clear all product data (e.g. on logout or forced refresh) */
    clearProducts(state) {
      state.rawDtos = [];
      state.error = null;
      state.initialized = false;
    },
  },
  extraReducers: builder => {
    builder
      .addCase(fetchProducts.pending, state => {
        state.loading = true;
        state.error = null;
      })
      .addCase(
        fetchProducts.fulfilled,
        (state, action: PayloadAction<ProductDto[]>) => {
          state.loading = false;
          state.rawDtos = action.payload;
          state.initialized = true;
        }
      )
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload ?? 'Unknown error';
      });
  },
});

export const { clearProducts } = productListSlice.actions;

// ── Selectors ──

const selectRawDtos = (state: RootState) => state.productList.rawDtos;
const selectLoading = (state: RootState) => state.productList.loading;
const selectError = (state: RootState) => state.productList.error;
const selectLangForProducts = (state: RootState) => state.preferences.lang;
const selectFilter = (state: RootState) => state.filter;

/**
 * Maps raw DTOs → localized Product[] based on current language.
 * Recomputes only when DTOs or language change (memoized).
 * Mirrors Angular ProductService.allProductsMapped$.
 */
export const selectMappedProducts = createSelector(
  [selectRawDtos, selectLangForProducts],
  (dtos, lang) => productMapper.toProductList(dtos, lang)
);

/**
 * Applies category + search + sort filters.
 * Mirrors Angular ProductFilterService.applyFiltersAndSort().
 */
function applyFiltersAndSort(
  products: Product[],
  filter: FilterState
): Product[] {
  let filtered = products;

  // 1. Category filter
  if (filter.selectedCategory && filter.selectedCategory !== 'all') {
    filtered = filtered.filter(p => p.category === filter.selectedCategory);
  }

  // 2. Search filter (case-insensitive, name + category)
  if (filter.searchQuery.trim()) {
    const q = filter.searchQuery.toLowerCase();
    filtered = filtered.filter(
      p =>
        p.name.toLowerCase().includes(q) ||
        p.category.toLowerCase().includes(q)
    );
  }

  // 3. Sort
  const sorted = [...filtered];
  switch (filter.sortBy) {
    case 'price-low':
      sorted.sort((a, b) => a.price - b.price);
      break;
    case 'price-high':
      sorted.sort((a, b) => b.price - a.price);
      break;
    case 'name':
    case 'name-asc':
      sorted.sort((a, b) => a.name.localeCompare(b.name));
      break;
    case 'name-desc':
      sorted.sort((a, b) => b.name.localeCompare(a.name));
      break;
    case 'featured':
    default:
      // No-op (keep original order)
      break;
  }

  return sorted;
}

/**
 * Full view-model for the Product List page.
 * Equivalent to Angular's `vm$ = combineLatest([...]).pipe(map(...))`.
 *
 * Returns a single object the page can consume with one useAppSelector call.
 */
export const selectProductListVm = createSelector(
  [selectMappedProducts, selectFilter, selectLoading, selectError],
  (products, filter, loading, error) => {
    const filtered = applyFiltersAndSort(products, filter);
    return {
      products: filtered,
      total: filtered.length,
      loading,
      error,
      filter,
      hasResults: filtered.length > 0,
      isEmpty: !loading && filtered.length === 0,
    };
  }
);

/** Featured products (for home page) */
export const selectFeaturedProducts = createSelector(
  [selectMappedProducts, (_: RootState, limit: number = 4) => limit],
  (products, limit) => products.slice(0, limit)
);

/** Related products (exclude current product id) */
export const selectRelatedProducts = createSelector(
  [
    selectMappedProducts,
    (_: RootState, params: { excludeId: number; limit?: number }) => params,
  ],
  (products, { excludeId, limit = 3 }) =>
    products.filter(p => p.id !== excludeId).slice(0, limit)
);

export default productListSlice.reducer;