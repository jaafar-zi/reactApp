import React, { useState } from 'react';
import { Box } from '@mui/material';
import { ASSET_PATHS } from '../../../shared/constants/assets';

interface ProductGalleryProps {
  mainImage: string;
  lifestyleImages: string[];
  productName: string;
}

export const ProductGallery: React.FC<ProductGalleryProps> = ({
  mainImage,
  lifestyleImages,
  productName,
}) => {
  const [featuredImage, setFeaturedImage] = useState(
    mainImage || ASSET_PATHS.images.placeholderProduct
  );

  const allImages = [mainImage, ...lifestyleImages].filter(Boolean);

  return (
    <Box>
      {/* Featured Image */}
      <Box
        sx={{
          aspectRatio: '1/1',
          overflow: 'hidden',
          bgcolor: '#f3f4f6',
          mb: '0.5rem',
        }}
      >
        <Box
          component="img"
          src={featuredImage}
          alt={productName}
          sx={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            objectPosition: 'center',
            display: 'block',
            transition: 'transform 600ms cubic-bezier(0.4, 0, 0.2, 1)',
            '&:hover': { transform: 'scale(0.98)' },
          }}
        />
      </Box>

      {/* Thumbnail Grid */}
      {allImages.length > 1 && (
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: `repeat(${Math.min(allImages.length, 4)}, 1fr)`,
            gap: '0.5rem',
          }}
        >
          {allImages.map((img, index) => (
            <Box
              key={index}
              onClick={() => setFeaturedImage(img)}
              sx={{
                aspectRatio: '1/1',
                overflow: 'hidden',
                cursor: 'pointer',
                border:
                  featuredImage === img
                    ? '2px solid #000'
                    : '1px solid #e5e7eb',
                bgcolor: '#f3f4f6',
                transition: 'border 300ms ease',
                '&:hover': { borderColor: '#000' },
              }}
            >
              <Box
                component="img"
                src={img}
                alt={`${productName} ${index + 1}`}
                loading="lazy"
                sx={{
                  width: '100%',
                  height: '100%',
                  objectFit: 'cover',
                  objectPosition: 'center',
                  display: 'block',
                }}
              />
            </Box>
          ))}
        </Box>
      )}
    </Box>
  );
};