import React from 'react';
import { Box, keyframes } from '@mui/material';

const shimmer = keyframes`
  0%   { background-position:  200% 0; }
  100% { background-position: -200% 0; }
`;

type SkeletonVariant = 'rect' | 'square' | 'text' | 'circle';

interface SkeletonProps {
  variant?: SkeletonVariant;
  width?: string | number;
  height?: string | number;
}

export const Skeleton: React.FC<SkeletonProps> = ({
  variant = 'rect',
  width = '100%',
  height = '1rem',
}) => {
  const baseSx = {
    background:
      'linear-gradient(90deg, #f3f4f6 25%, #e5e7eb 50%, #f3f4f6 75%)',
    backgroundSize: '200% 100%',
    animation: `${shimmer} 1.5s infinite`,
  };

  switch (variant) {
    case 'square':
      return (
        <Box
          aria-hidden
          role="presentation"
          sx={{ ...baseSx, width: '100%', aspectRatio: '1/1' }}
        />
      );
    case 'circle':
      return (
        <Box
          aria-hidden
          role="presentation"
          sx={{ ...baseSx, width, height, borderRadius: '9999px' }}
        />
      );
    case 'text':
      return (
        <Box
          aria-hidden
          role="presentation"
          sx={{ ...baseSx, width, height: '1rem', borderRadius: '0.25rem' }}
        />
      );
    default:
      return (
        <Box
          aria-hidden
          role="presentation"
          sx={{ ...baseSx, width, height, borderRadius: 0 }}
        />
      );
  }
};