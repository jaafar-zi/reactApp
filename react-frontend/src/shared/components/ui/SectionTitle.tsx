import React from 'react';
import { Typography, type TypographyProps } from '@mui/material';

type TitleSize =
  | 'hero'
  | 'display'
  | 'section'
  | 'subsection'
  | 'card'
  | 'label';
type TitleWeight = 'light' | 'normal' | 'medium' | 'bold';
type TitleColor = 'default' | 'light' | 'muted';

const sizeMap: Record<TitleSize, TypographyProps['sx']> = {
  hero: {
    fontSize: {
      xs: '1.75rem',
      sm: 'clamp(2rem, 8vw, 3rem)',
      md: 'clamp(2.5rem, 6vw, 5rem)',
    },
    lineHeight: 0.95,
    letterSpacing: '-0.02em',
  },
  display: {
    fontSize: { xs: '1.125rem', sm: '1.25rem', md: '1.875rem', lg: '1.5rem' },
    lineHeight: 1,
    letterSpacing: '0.05em',
  },
  section: {
    fontSize: {
      xs: '1rem',
      sm: 'clamp(1.125rem, 4vw, 1.5rem)',
      md: 'clamp(1.5rem, 3vw, 2.5rem)',
    },
    lineHeight: 1.25,
    letterSpacing: '-0.02em',
  },
  subsection: {
    fontSize: '1.25rem',
    lineHeight: 1.3,
    letterSpacing: '0.02em',
  },
  card: {
    fontSize: { xs: '1rem', sm: '1.125rem', md: '1.25rem' },
    lineHeight: 1.25,
    letterSpacing: '0.05em',
  },
  label: {
    fontSize: '0.875rem',
    lineHeight: 1.4,
    letterSpacing: '0.15em',
  },
};

const weightMap: Record<TitleWeight, number> = {
  light: 300,
  normal: 400,
  medium: 500,
  bold: 700,
};

const colorMap: Record<TitleColor, string> = {
  default: '#000',
  light: '#fff',
  muted: '#6b7280',
};

interface SectionTitleProps {
  level?: 'h1' | 'h2' | 'h3' | 'h4';
  size?: TitleSize;
  weight?: TitleWeight;
  alignment?: 'left' | 'center' | 'right';
  color?: TitleColor;
  uppercase?: boolean;
  children: React.ReactNode;
}

export const SectionTitle: React.FC<SectionTitleProps> = ({
  level = 'h2',
  size = 'section',
  weight = 'bold',
  alignment = 'left',
  color = 'default',
  uppercase = true,
  children,
}) => (
  <Typography
    component={level}
    sx={{
      ...sizeMap[size],
      fontWeight: weightMap[weight],
      color: colorMap[color],
      textAlign: alignment,
      textTransform: uppercase ? 'uppercase' : 'none',
      margin: 0,
      fontFamily: "'DM Sans', sans-serif",
    }}
  >
    {children}
  </Typography>
);