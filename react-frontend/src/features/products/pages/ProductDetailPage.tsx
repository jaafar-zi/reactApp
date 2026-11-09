import React, { useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, CircularProgress } from '@mui/material';
import { useTranslation } from 'react-i18next';
import {
  fetchProductDetail,
  setSize,
  setColor,
  setQuantity,
  setAddToCartStatus,
  clearProductDetail,
  selectProductDetail,
  selectProductDetailLoading,
  selectProductDetailError,
  selectProductSelection,
  selectAddToCartStatus,
  selectCanAddToCart,
} from '../slice/productDetailSlice';
import { addItem } from '../../cart/slice/cartSlice';
import { selectLang, selectCurrency } from '../../preferences/slice/preferencesSlice';
import { ProductGallery } from '../components/ProductGallery';
import { ProductInfo } from '../components/ProductInfo';
import { SectionContainer } from '../../../shared/components/ui/SectionContainer';
import { EmptyState } from '../../../shared/components/ui/EmptyState';
import { APP_ROUTES } from '../../../shared/constants/routes';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';

export const ProductDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { t } = useTranslation('products');

  const product = useAppSelector(selectProductDetail);
  const loading = useAppSelector(selectProductDetailLoading);
  const error = useAppSelector(selectProductDetailError);
  const selection = useAppSelector(selectProductSelection);
  const addToCartStatus = useAppSelector(selectAddToCartStatus);
  const canAddToCart = useAppSelector(selectCanAddToCart);
  const lang = useAppSelector(selectLang);
  const currency = useAppSelector(selectCurrency);

  // Fetch product on mount and when id/lang changes
  useEffect(() => {
    if (id) {
      dispatch(fetchProductDetail({ id: Number(id), lang }));
    }
    return () => {
      dispatch(clearProductDetail());
    };
  }, [dispatch, id, lang]);

  // Re-fetch when language changes (mirrors Angular lang$ switchMap)
  useEffect(() => {
    if (id && product) {
      dispatch(fetchProductDetail({ id: Number(id), lang }));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [lang]);

  const handleAddToCart = useCallback(() => {
    if (!product || !canAddToCart) return;

    dispatch(
      addItem({
        id: `${product.id}-${selection.size}-${selection.color}`,
        productId: String(product.id),
        name: product.name,
        price: product.price,
        quantity: selection.quantity,
        image: product.image,
        size: selection.size ?? undefined,
        color: selection.color ?? undefined,
        sku: product.sku,
      })
    );

    dispatch(setAddToCartStatus({ status: 'success' }));

    // Auto-reset after 3s (mirrors Angular ProductDetailFacade)
    setTimeout(() => {
      dispatch(setAddToCartStatus({ status: 'idle' }));
    }, 3000);
  }, [dispatch, product, canAddToCart, selection]);

  const handleBuyNow = useCallback(() => {
    handleAddToCart();
    navigate(APP_ROUTES.checkout);
  }, [handleAddToCart, navigate]);

  const currencySymbol = currency === 'TND' ? 'TND' : currency;

  // Loading
  if (loading) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '80vh',
          pt: { xs: '4.5rem', sm: '5rem', md: '6rem' },
        }}
      >
        <CircularProgress size={32} sx={{ color: '#000' }} />
      </Box>
    );
  }

  // Error / Not Found
  if (error || !product) {
    return (
      <Box sx={{ pt: { xs: '4.5rem', sm: '5rem', md: '6rem' } }}>
        <EmptyState
          title={error ?? t('productNotFound')}
          ctaLabel={t('backToProducts')}
          onCtaClick={() => navigate(APP_ROUTES.collections.all)}
        />
      </Box>
    );
  }

  return (
    <Box
      sx={{
        pt: { xs: '4.5rem', sm: '5rem', md: '6rem' },
        minHeight: '100vh',
      }}
    >
      <SectionContainer padding="lg">
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' },
            gap: { xs: '2rem', md: '4rem' },
          }}
        >
          {/* Gallery */}
          <ProductGallery
            mainImage={product.mainImage}
            lifestyleImages={product.lifestyleImages}
            productName={product.name}
          />

          {/* Info */}
          <ProductInfo
            product={product}
            selection={selection}
            canAddToCart={canAddToCart}
            addToCartStatus={addToCartStatus}
            addToCartError={null}
            currencySymbol={currencySymbol}
            onSizeChange={size => dispatch(setSize(size))}
            onColorChange={color => dispatch(setColor(color))}
            onQuantityChange={qty => dispatch(setQuantity(qty))}
            onAddToCart={handleAddToCart}
            onBuyNow={handleBuyNow}
          />
        </Box>
      </SectionContainer>
    </Box>
  );
};