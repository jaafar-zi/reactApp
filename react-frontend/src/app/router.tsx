import React, { Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import { Box, CircularProgress } from '@mui/material';
import { ProductDetailPage } from '../features/products/pages/ProductDetailPage';
import { ProductListPage } from '../features/products/pages/ProductListPage';
import { APP_ROUTES } from '../shared/constants/routes';
import AppLayout from '../AppLayout';
import Login from '../features/auth/pages/auth/Login';
import Register from '../features/auth/pages/auth/Register';
import Profile from '../features/auth/pages/profile/Profile';
import ProtectedRoute from '../shared/components/ProtectedRoute';

const Loader = () => (
  <Box sx={{
    display: 'flex', justifyContent: 'center',
    alignItems: 'center', minHeight: '60vh',
  }}>
    <CircularProgress />
  </Box>
);

const AppRouter: React.FC = () => (
  <Suspense fallback={<Loader />}>
    <Routes>
      {/* Pages WITH navbar + footer */}
      <Route element={<AppLayout />}>
        <Route path={APP_ROUTES.home} element={<ProductListPage />} />
        <Route path={APP_ROUTES.collections.all} element={<ProductListPage />} />
        <Route path={APP_ROUTES.collections.detail()} element={<ProductDetailPage />} />
        <Route
          path={APP_ROUTES.profile}
          element={
            <ProtectedRoute>
              <Profile />
            </ProtectedRoute>
          }
        />
      </Route>

      {/* Auth pages WITHOUT navbar + footer (clean layout) */}
      <Route path={APP_ROUTES.auth.login} element={<Login />} />
      <Route path={APP_ROUTES.auth.register} element={<Register />} />
    </Routes>
  </Suspense>
);

export default AppRouter;