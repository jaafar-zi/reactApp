import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { Cart, CartItem } from '../types/cart.types';

const CART_STORAGE_KEY = 'cart';

// ── Helpers (mirror Angular CartService exactly) ──

function getEmptyCart(): Cart {
  return {
    items: [],
    total: 0,
    subtotal: 0,
    currency: 'TND',
    itemCount: 0,
  };
}

function loadCartFromStorage(): Cart {
  if (typeof window === 'undefined') return getEmptyCart();
  try {
    const saved = localStorage.getItem(CART_STORAGE_KEY);
    return saved ? JSON.parse(saved) : getEmptyCart();
  } catch {
    return getEmptyCart();
  }
}

function saveCartToStorage(cart: Cart): void {
  if (typeof window === 'undefined') return;
  try {
    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cart));
  } catch {
    /* noop */
  }
}

function calcTotals(items: CartItem[]) {
  const subtotal = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const itemCount = items.reduce((sum, item) => sum + item.quantity, 0);
  return { subtotal, total: subtotal, itemCount };
}

/**
 * Two cart items are "the same" if they share
 * id + size + color + sku (mirrors Angular isSameCartItem).
 */
function isSameItem(a: CartItem, b: CartItem): boolean {
  return (
    a.id === b.id &&
    a.size === b.size &&
    a.color === b.color &&
    a.sku === b.sku
  );
}

// ── State ──

interface CartState extends Cart {
  isOpen: boolean;
}

const initialState: CartState = {
  ...loadCartFromStorage(),
  isOpen: false,
};

// ── Slice ──

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    /**
     * Add item to cart.
     * If same item (id+size+color+sku) exists, increments quantity.
     */
    addItem(state, action: PayloadAction<CartItem>) {
      const item = action.payload;
      const idx = state.items.findIndex(i => isSameItem(i, item));

      if (idx > -1) {
        state.items[idx].quantity += item.quantity;
      } else {
        state.items.push(item);
      }

      Object.assign(state, calcTotals(state.items));
      saveCartToStorage(state);
    },

    /**
     * Remove item by id.
     */
    removeItem(state, action: PayloadAction<string>) {
      state.items = state.items.filter(i => i.id !== action.payload);
      Object.assign(state, calcTotals(state.items));
      saveCartToStorage(state);
    },

    /**
     * Update quantity. If quantity <= 0, removes the item.
     */
    updateQuantity(
      state,
      action: PayloadAction<{ itemId: string; quantity: number }>
    ) {
      const { itemId, quantity } = action.payload;

      if (quantity <= 0) {
        state.items = state.items.filter(i => i.id !== itemId);
      } else {
        const item = state.items.find(i => i.id === itemId);
        if (item) item.quantity = quantity;
      }

      Object.assign(state, calcTotals(state.items));
      saveCartToStorage(state);
    },

    /**
     * Clear entire cart.
     */
    clearCart(state) {
      state.items = [];
      Object.assign(state, calcTotals([]));
      if (typeof window !== 'undefined') {
        localStorage.removeItem(CART_STORAGE_KEY);
      }
    },

    // ── Drawer open/close (mirrors Angular cartOpen$) ──

    toggleCart(state) {
      state.isOpen = !state.isOpen;
    },
    openCart(state) {
      state.isOpen = true;
    },
    closeCart(state) {
      state.isOpen = false;
    },
  },
});

export const {
  addItem,
  removeItem,
  updateQuantity,
  clearCart,
  toggleCart,
  openCart,
  closeCart,
} = cartSlice.actions;

// ── Selectors ──

export const selectCartItems = (state: { cart: CartState }) => state.cart.items;
export const selectCartCount = (state: { cart: CartState }) => state.cart.itemCount;
export const selectCartTotal = (state: { cart: CartState }) => state.cart.total;
export const selectCartIsOpen = (state: { cart: CartState }) => state.cart.isOpen;

export default cartSlice.reducer;