import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { FilterState, SortValue } from '../types/product.types';

const STORAGE_KEY = 'productFilters';

// ── sessionStorage helpers (mirrors Angular FilterService) ──

function loadFromStorage(): Partial<FilterState> {
  if (typeof window === 'undefined') return {};
  try {
    const saved = sessionStorage.getItem(STORAGE_KEY);
    return saved ? JSON.parse(saved) : {};
  } catch (err) {
    console.error('Error loading filters from storage:', err);
    return {};
  }
}

function saveToStorage(state: FilterState): void {
  if (typeof window === 'undefined') return;
  try {
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  } catch (err) {
    console.error('Error saving filters to storage:', err);
  }
}

function clearStorage(): void {
  if (typeof window === 'undefined') return;
  try {
    sessionStorage.removeItem(STORAGE_KEY);
  } catch (err) {
    console.error('Error clearing filters from storage:', err);
  }
}

// ── Initial State (hydrate from sessionStorage if available) ──

const stored = loadFromStorage();

const initialState: FilterState = {
  selectedCategory: stored.selectedCategory ?? 'all',
  sortBy: (stored.sortBy as SortValue) ?? 'featured',
  searchQuery: stored.searchQuery ?? '',
};

// ── Slice ──

const filterSlice = createSlice({
  name: 'filter',
  initialState,
  reducers: {
    setCategory(state, action: PayloadAction<string>) {
      state.selectedCategory = action.payload;
      saveToStorage(state);
    },

    setSortBy(state, action: PayloadAction<SortValue>) {
      state.sortBy = action.payload;
      saveToStorage(state);
    },

    setSearchQuery(state, action: PayloadAction<string>) {
      state.searchQuery = action.payload;
      saveToStorage(state);
    },

    resetFilters(state) {
      state.selectedCategory = 'all';
      state.sortBy = 'featured';
      state.searchQuery = '';
      clearStorage();
    },

    resetCategory(state) {
      state.selectedCategory = 'all';
      saveToStorage(state);
    },

    resetSort(state) {
      state.sortBy = 'featured';
      saveToStorage(state);
    },

    resetSearch(state) {
      state.searchQuery = '';
      saveToStorage(state);
    },
  },
});

export const {
  setCategory,
  setSortBy,
  setSearchQuery,
  resetFilters,
  resetCategory,
  resetSort,
  resetSearch,
} = filterSlice.actions;

// ── Selectors ──

export const selectFilterState = (state: { filter: FilterState }) => state.filter;

export const selectActiveFilterCount = (state: { filter: FilterState }) => {
  let count = 0;
  if (state.filter.selectedCategory !== 'all') count++;
  if (state.filter.sortBy !== 'featured') count++;
  if (state.filter.searchQuery.trim() !== '') count++;
  return count;
};

export default filterSlice.reducer;