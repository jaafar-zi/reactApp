import { combineReducers } from '@reduxjs/toolkit';
import preferencesReducer from '../features/preferences/slice/preferencesSlice';
import cartReducer from '../features/cart/slice/cartSlice';
import filterReducer from '../features/products/slice/filterSlice';
import productListReducer from '../features/products/slice/productListSlice';
import productDetailReducer from '../features/products/slice/productDetailSlice';

const rootReducer = combineReducers({
  preferences: preferencesReducer,
  cart: cartReducer,
  filter: filterReducer,
  productList: productListReducer,
  productDetail: productDetailReducer,
});

export type RootState = ReturnType<typeof rootReducer>;

export default rootReducer;