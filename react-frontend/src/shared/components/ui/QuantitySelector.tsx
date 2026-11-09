import React from 'react';
import { Box, IconButton, Typography } from '@mui/material';

interface QuantitySelectorProps {
  quantity: number;
  min?: number;
  max?: number;
  onChange: (qty: number) => void;
}

export const QuantitySelector: React.FC<QuantitySelectorProps> = ({
  quantity,
  min = 1,
  max = 99,
  onChange,
}) => (
  <Box
    sx={{
      display: 'inline-flex',
      alignItems: 'center',
      border: '1px solid #d1d5db',
    }}
  >
    <IconButton
      size="small"
      disabled={quantity <= min}
      onClick={() => onChange(Math.max(min, quantity - 1))}
      aria-label="Decrease quantity"
      sx={{
        borderRadius: 0,
        px: '0.75rem',
        py: '0.5rem',
        fontSize: '1rem',
        '&:hover': { bgcolor: '#f3f4f6' },
      }}
    >
      −
    </IconButton>

    <Typography
      aria-live="polite"
      sx={{
        px: '1rem',
        py: '0.5rem',
        fontSize: '0.875rem',
        fontWeight: 500,
        minWidth: '2.5rem',
        textAlign: 'center',
        borderLeft: '1px solid #d1d5db',
        borderRight: '1px solid #d1d5db',
      }}
    >
      {quantity}
    </Typography>

    <IconButton
      size="small"
      disabled={quantity >= max}
      onClick={() => onChange(Math.min(max, quantity + 1))}
      aria-label="Increase quantity"
      sx={{
        borderRadius: 0,
        px: '0.75rem',
        py: '0.5rem',
        fontSize: '1rem',
        '&:hover': { bgcolor: '#f3f4f6' },
      }}
    >
      +
    </IconButton>
  </Box>
);