import React, { useRef, useState, useCallback } from 'react';
import { Box, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import type { Product } from '../types/product.types';
import { APP_ROUTES } from '../../../shared/constants/routes';
import CursorHoverElement from '../../../shared/components/ui/CursorHoverElement';

interface ProductCardProps {
  product: Product;
  showQuickView?: boolean;
  showAddToCart?: boolean;
  showWishlist?: boolean;
  imageAspectRatio?: string;
  currencySymbol?: string;
  showBadge?: boolean;
  badgeText?: string;
  badgeColor?: string;
  loading?: boolean;
  onQuickView?: (product: Product) => void;
  onAddToCart?: (product: Product) => void;
  onAddToWishlist?: (product: Product) => void;
}

export const ProductCard: React.FC<ProductCardProps> = ({
  product,
  imageAspectRatio = '3/4',
  currencySymbol = 'TND',
  showBadge = false,
  badgeText,
  badgeColor = '#000',
  loading = false,
  onAddToCart,
}) => {
  const navigate = useNavigate();
  const { t } = useTranslation('products');
  const cardRef = useRef<HTMLDivElement>(null);
  const [isHovered, setIsHovered] = useState(false);

  const handleClick = useCallback(() => {
    navigate(APP_ROUTES.collections.detail(String(product.id)));
  }, [navigate, product.id]);

  const handleMouseEnter = useCallback(() => setIsHovered(true), []);
  const handleMouseLeave = useCallback(() => setIsHovered(false), []);

  if (loading) {
    return (
      <Box sx={{ cursor: 'default' }}>
        <Box
          sx={{
            aspectRatio: imageAspectRatio,
            bgcolor: '#f3f4f6',
            mb: '0.75rem',
          }}
        />
        <Box
          sx={{
            height: '0.75rem',
            width: '60%',
            bgcolor: '#f3f4f6',
            mb: '0.5rem',
          }}
        />
        <Box sx={{ height: '0.75rem', width: '30%', bgcolor: '#f3f4f6' }} />
      </Box>
    );
  }

  return (
    <Box
      ref={cardRef}
      onClick={handleClick}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      sx={{
        cursor: 'pointer',
        position: 'relative',
        overflow: 'hidden',
      }}
    >
      {/* Image Container */}
      <Box
        sx={{
          position: 'relative',
          aspectRatio: imageAspectRatio,
          overflow: 'hidden',
          mb: '0.75rem',
          bgcolor: '#f3f4f6',
        }}
      >
        {/* Primary Image */}
        <Box
          component="img"
          src={isHovered && product.hoverImage ? product.hoverImage : product.image}
          alt={product.name}
          loading="lazy"
          sx={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            objectPosition: 'center',
            display: 'block',
            transition: 'transform 600ms cubic-bezier(0.4, 0, 0.2, 1)',
            transform: isHovered ? 'scale(1.05)' : 'scale(1)',
          }}
        />

        {/* Hover Overlay */}
        <Box
          sx={{
            position: 'absolute',
            inset: 0,
            bgcolor: isHovered ? 'rgba(0,0,0,0.2)' : 'rgba(0,0,0,0)',
            transition: 'background 600ms cubic-bezier(0.4, 0, 0.2, 1)',
            pointerEvents: 'none',
          }}
        />

        {/* Badge */}
        {showBadge && badgeText && (
          <Box
            sx={{
              position: 'absolute',
              top: '0.5rem',
              left: '0.5rem',
              bgcolor: badgeColor,
              color: '#fff',
              px: '0.5rem',
              py: '0.25rem',
              fontSize: '0.625rem',
              textTransform: 'uppercase',
              letterSpacing: '0.1em',
            }}
          >
            {badgeText}
          </Box>
        )}

        {/* Cursor Hover Element */}
        <CursorHoverElement
          isVisible={isHovered}
          containerRef={cardRef}
          referenceText={`(OR-${product.id})`}
          actionText={t('viewProduct')}
        />
      </Box>

      {/* Product Info */}
      <Box>
        <Typography
          sx={{
            fontSize: '0.75rem',
            fontWeight: 500,
            textTransform: 'uppercase',
            letterSpacing: '0.05em',
            mb: '0.25rem',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
          }}
        >
          {product.name}
        </Typography>
        <Typography
          sx={{
            fontSize: '0.75rem',
            color: '#6b7280',
          }}
        >
          {product.price.toFixed(3)} {currencySymbol}
        </Typography>
      </Box>
    </Box>
  );
};