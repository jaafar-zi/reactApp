import React from 'react';
import { Box } from '@mui/material';

type BgVariant = 'default' | 'muted' | 'dark' | 'transparent';
type PaddingVariant = 'none' | 'sm' | 'md' | 'lg' | 'xl';
type MaxWidthVariant = 'sm' | 'md' | 'lg' | 'xl' | 'full' | 'none';

const bgMap: Record<BgVariant, string> = {
  default: '#ffffff',
  muted: '#f9fafb',
  dark: '#111827',
  transparent: 'transparent',
};

const padMap: Record<PaddingVariant, { py: string; pySm: string; pyXs: string }> = {
  none: { py: '0', pySm: '0', pyXs: '0' },
  sm: { py: '2rem', pySm: '1rem', pyXs: '1rem' },
  md: { py: '3rem', pySm: '2rem', pyXs: '2rem' },
  lg: { py: '4rem', pySm: '3rem', pyXs: '3rem' },
  xl: { py: '6rem', pySm: '4rem', pyXs: '2rem' },
};

const maxWMap: Record<MaxWidthVariant, string> = {
  sm: '640px',
  md: '100%',
  lg: '100%',
  xl: '100%',
  full: '100%',
  none: 'none',
};

interface SectionContainerProps {
  variant?: BgVariant;
  maxWidth?: MaxWidthVariant;
  padding?: PaddingVariant;
  centered?: boolean;
  ariaLabel?: string;
  children: React.ReactNode;
}

export const SectionContainer: React.FC<SectionContainerProps> = ({
  variant = 'default',
  maxWidth = 'lg',
  padding = 'xl',
  centered = false,
  ariaLabel,
  children,
}) => {
  const p = padMap[padding];

  return (
    <Box
      component="section"
      aria-label={ariaLabel}
      sx={{
        width: '100%',
        bgcolor: bgMap[variant],
        color: variant === 'dark' ? '#fff' : 'inherit',
        py: { xs: p.pyXs, sm: p.pySm, md: p.py },
      }}
    >
      <Box
        sx={{
          maxWidth: maxWMap[maxWidth],
          mx: 'auto',
          px: { xs: '1rem', sm: '2rem' },
          textAlign: centered ? 'center' : 'inherit',
        }}
      >
        {children}
      </Box>
    </Box>
  );
};