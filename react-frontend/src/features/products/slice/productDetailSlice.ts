import {
  createSlice,
  createAsyncThunk,
  type PayloadAction,
} from '@reduxjs/toolkit';
import type {
  ProductDetail,
  ProductSelection,
} from '../types/product.types';
import { productService } from '../service/productService';

type AddToCartStatus = 'idle' | 'success' | 'error';

interface ProductDetailState {
  product: ProductDetail | null;
  loading: boolean;
  error: string | null;

  /** Current user selection (size/color/quantity) */
  selection: ProductSelection;

  /** Feedback after Add-to-Cart click (auto-reset after 3s in component) */
  addToCartStatus: AddToCartStatus;
  addToCartError: string | null;
}

const initialSelection: ProductSelection = {
  size: null,
  color: null,
  quantity: 1,
};

const initialState: ProductDetailState = {
  product: null,
  loading: false,
  error: null,
  selection: initialSelection,
  addToCartStatus: 'idle',
  addToCartError: null,
};

// ── Async Thunks ──

/**
 * Fetches a single product detail by id, mapped to current language.
 * Mirrors Angular ProductService.getProductDetailById().
 */
export const fetchProductDetail = createAsyncThunk<
  ProductDetail,
  { id: number; lang: string },
  { rejectValue: string }
>(
  'products/fetchDetail',
  async ({ id, lang }, { rejectWithValue }) => {
    try {
      const product = await productService.getProductDetailById(id, lang);
      if (!product) {
        return rejectWithValue('Product not found');
      }
      return product;
    } catch (err: any) {
      console.error(
        `[productDetailSlice] Failed to fetch product #${id}:`,
        err
      );
      return rejectWithValue(err?.message ?? 'Failed to fetch product');
    }
  }
);

// ── Slice ──

const productDetailSlice = createSlice({
  name: 'productDetail',
  initialState,
  reducers: {
    /** Set size selection */
    setSize(state, action: PayloadAction<string | null>) {
      state.selection.size = action.payload;
    },

    /** Set color selection */
    setColor(state, action: PayloadAction<string | null>) {
      state.selection.color = action.payload;
    },

    /** Set quantity (clamped 1–99) */
    setQuantity(state, action: PayloadAction<number>) {
      const qty = Math.max(1, Math.min(99, action.payload));
      state.selection.quantity = qty;
    },

    /** Reset selection to defaults */
    resetSelection(state) {
      state.selection = { ...initialSelection };
    },

    /**
     * Initialize selection based on product defaults.
     * Mirrors Angular ProductDetailFacade.initializeSelection().
     */
    initializeSelection(state, action: PayloadAction<ProductDetail>) {
      const product = action.payload;
      state.selection = {
        size: product.sizes?.length === 1 ? product.sizes[0] : null,
        color: product.colors?.length ? product.colors[0] : null,
        quantity: 1,
      };
    },

    /** Set Add-to-Cart feedback status */
    setAddToCartStatus(
      state,
      action: PayloadAction<{ status: AddToCartStatus; error?: string }>
    ) {
      state.addToCartStatus = action.payload.status;
      state.addToCartError = action.payload.error ?? null;
    },

    /** Clear product detail (on unmount) */
    clearProductDetail(state) {
      state.product = null;
      state.error = null;
      state.selection = { ...initialSelection };
      state.addToCartStatus = 'idle';
      state.addToCartError = null;
    },
  },
  extraReducers: builder => {
    builder
      .addCase(fetchProductDetail.pending, state => {
        state.loading = true;
        state.error = null;
        state.product = null;
      })
      .addCase(
        fetchProductDetail.fulfilled,
        (state, action: PayloadAction<ProductDetail>) => {
          state.loading = false;
          state.product = action.payload;

          // Auto-initialize selection (mirrors Angular initializeSelection)
          const product = action.payload;
          state.selection = {
            size: product.sizes?.length === 1 ? product.sizes[0] : null,
            color: product.colors?.length ? product.colors[0] : null,
            quantity: 1,
          };
        }
      )
      .addCase(fetchProductDetail.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload ?? 'Unknown error';
        state.product = null;
      });
  },
});

export const {
  setSize,
  setColor,
  setQuantity,
  resetSelection,
  initializeSelection,
  setAddToCartStatus,
  clearProductDetail,
} = productDetailSlice.actions;

// ── Selectors ──

export const selectProductDetail = (state: {
  productDetail: ProductDetailState;
}) => state.productDetail.product;

export const selectProductDetailLoading = (state: {
  productDetail: ProductDetailState;
}) => state.productDetail.loading;

export const selectProductDetailError = (state: {
  productDetail: ProductDetailState;
}) => state.productDetail.error;

export const selectProductSelection = (state: {
  productDetail: ProductDetailState;
}) => state.productDetail.selection;

export const selectAddToCartStatus = (state: {
  productDetail: ProductDetailState;
}) => state.productDetail.addToCartStatus;

/**
 * Can the user add this product to cart?
 * - Must be in stock
 * - If sizes exist, a size must be selected
 * - If colors exist, a color must be selected
 */
export const selectCanAddToCart = (state: {
  productDetail: ProductDetailState;
}): boolean => {
  const { product, selection } = state.productDetail;
  if (!product || !product.inStock) return false;
  if (product.sizes?.length && !selection.size) return false;
  if (product.colors?.length && !selection.color) return false;
  return true;
};

export default productDetailSlice.reducer;