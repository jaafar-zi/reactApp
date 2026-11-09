import React from 'react';
import { Box, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import type { ProductDetail, ProductSelection } from '../types/product.types';
import { Accordion } from '../../../shared/components/ui/Accordion';
import { OptionSelector } from '../../../shared/components/ui/OptionSelector';
import { QuantitySelector } from '../../../shared/components/ui/QuantitySelector';
import { StatusMessage } from '../../../shared/components/ui/StatusMessage';
import { Button } from '../../../shared/components/ui/Button';

interface ProductInfoProps {
  product: ProductDetail;
  selection: ProductSelection;
  canAddToCart: boolean;
  addToCartStatus: 'idle' | 'success' | 'error';
  addToCartError: string | null;
  currencySymbol?: string;
  onSizeChange: (size: string | null) => void;
  onColorChange: (color: string | null) => void;
  onQuantityChange: (qty: number) => void;
  onAddToCart: () => void;
  onBuyNow: () => void;
}

export const ProductInfo: React.FC<ProductInfoProps> = ({
  product,
  selection,
  canAddToCart,
  addToCartStatus,
  addToCartError,
  currencySymbol = 'TND',
  onSizeChange,
  onColorChange,
  onQuantityChange,
  onAddToCart,
  onBuyNow,
}) => {
  const { t } = useTranslation('products');

  return (
    <Box
      sx={{
        position: { md: 'sticky' },
        top: { md: '6rem' },
        maxHeight: { md: 'calc(100vh - 12rem)' },
        overflowY: { md: 'auto' },
        // Custom scrollbar (mirrors Angular @include custom-scrollbar)
        '&::-webkit-scrollbar': { width: '6px' },
        '&::-webkit-scrollbar-track': { background: '#f1f1f1' },
        '&::-webkit-scrollbar-thumb': {
          background: '#888',
          borderRadius: '3px',
          '&:hover': { background: '#555' },
        },
      }}
    >
      {/* Title + Price */}
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: '1fr auto',
          alignItems: 'start',
          gap: '1rem',
          mb: '1.5rem',
        }}
      >
        <Typography
          component="h1"
          sx={{
            fontSize: { xs: '1rem', sm: '1.25rem' },
            fontWeight: 700,
            textTransform: 'uppercase',
            letterSpacing: '0.02em',
            lineHeight: 1.25,
          }}
        >
          {product.name}
        </Typography>
        <Typography
          sx={{
            fontSize: '1rem',
            fontWeight: 500,
            whiteSpace: 'nowrap',
          }}
        >
          {product.price.toFixed(3)} {currencySymbol}
        </Typography>
      </Box>

      {/* Description */}
      {product.description && (
        <Box
          sx={{
            fontSize: '0.875rem',
            lineHeight: 1.75,
            color: '#374151',
            mb: '1.5rem',
          }}
          dangerouslySetInnerHTML={{ __html: product.description }}
        />
      )}

      {/* Size Selector */}
      {product.sizes?.length > 0 && (
        <OptionSelector
          label={t('size')}
          options={product.sizes}
          selected={selection.size}
          onChange={onSizeChange}
          disabled={!product.inStock}
        />
      )}

      {/* Color Selector */}
      {product.colors?.length > 0 && (
        <OptionSelector
          label={t('color')}
          options={product.colors}
          selected={selection.color}
          onChange={onColorChange}
          disabled={!product.inStock}
        />
      )}

      {/* Quantity */}
      <Box sx={{ py: '1rem' }}>
        <Typography
          sx={{
            fontSize: '0.75rem',
            fontWeight: 500,
            textTransform: 'uppercase',
            letterSpacing: '0.1em',
            mb: '0.75rem',
          }}
        >
          {t('quantity')}
        </Typography>
        <QuantitySelector
          quantity={selection.quantity}
          onChange={onQuantityChange}
        />
      </Box>

      {/* Status Messages */}
      {!product.inStock && (
        <StatusMessage variant="error" message={t('outOfStock')} />
      )}
      {addToCartStatus === 'success' && (
        <StatusMessage variant="success" message={t('addedToCart')} />
      )}
      {addToCartStatus === 'error' && addToCartError && (
        <StatusMessage variant="error" message={addToCartError} />
      )}
      {product.inStock &&
        product.sizes?.length > 0 &&
        !selection.size &&
        addToCartStatus === 'idle' && (
          <StatusMessage variant="warning" message={t('selectSize')} />
        )}

      {/* Action Buttons */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: '0.75rem', mt: '1.5rem' }}>
        <Button
          variant="primary"
          size="large"
          fullWidth
          disabled={!canAddToCart}
          onClick={onAddToCart}
        >
          {t('addToCart')}
        </Button>
        <Button
          variant="outline"
          size="large"
          fullWidth
          disabled={!canAddToCart}
          onClick={onBuyNow}
        >
          {t('buyNow')}
        </Button>
      </Box>

      {/* Attributes Accordions */}
      {product.attributes?.length > 0 && (
        <Box sx={{ mt: '2rem' }}>
          {product.attributes.map(attr => (
            <Accordion key={attr.id} itemId={attr.id} title={attr.title}>
              {attr.content}
            </Accordion>
          ))}
        </Box>
      )}

      {/* Garment Details */}
      {product.garmentDetails && (
        <Box sx={{ mt: '1rem' }}>
          <Accordion itemId="garment" title={t('garmentDetails')}>
            {product.garmentDetails}
          </Accordion>
        </Box>
      )}

      {/* Wash Care */}
      {product.washCare && (
        <Box sx={{ mt: '0' }}>
          <Accordion itemId="washcare" title={t('washCare')}>
            <Box component="pre" sx={{ whiteSpace: 'pre-wrap', fontFamily: 'inherit', margin: 0 }}>
              {product.washCare}
            </Box>
          </Accordion>
        </Box>
      )}

      {/* Collection / Origin / Edition */}
      {product.collection && (
        <Box sx={{ mt: '1rem', fontSize: '0.75rem', color: '#6b7280' }}>
          <Typography sx={{ textTransform: 'uppercase', letterSpacing: '0.1em' }}>
            {t('collection')}: {product.collection}
          </Typography>
        </Box>
      )}
    </Box>
  );
};
