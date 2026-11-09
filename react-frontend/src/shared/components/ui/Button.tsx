import React from 'react';
import {
  Button as MuiButton,
  CircularProgress,
  type ButtonProps as MuiButtonProps,
} from '@mui/material';
import { Link } from 'react-router-dom';

type ButtonVariant = 'primary' | 'outline' | 'text';
type ButtonSize = 'small' | 'medium' | 'large';

interface ButtonProps {
  variant?: ButtonVariant;
  size?: ButtonSize;
  loading?: boolean;
  fullWidth?: boolean;
  href?: string;
  to?: string;
  type?: 'button' | 'submit' | 'reset';
  disabled?: boolean;
  ariaLabel?: string;
  showContentWhileLoading?: boolean;
  onClick?: (e: React.MouseEvent) => void;
  children: React.ReactNode;
  sx?: MuiButtonProps['sx'];
}

const sizeMap: Record<
  ButtonSize,
  { px: string; py: string; minH: string; fontSize: string }
> = {
  small: { px: '0.75rem', py: '0.5rem', minH: '40px', fontSize: '0.75rem' },
  medium: { px: '1rem', py: '0.75rem', minH: '44px', fontSize: '0.75rem' },
  large: { px: '1.25rem', py: '0.75rem', minH: '48px', fontSize: '0.875rem' },
};

export const Button: React.FC<ButtonProps> = ({
  variant = 'outline',
  size = 'medium',
  loading = false,
  fullWidth = false,
  href,
  to,
  type = 'button',
  disabled = false,
  ariaLabel,
  showContentWhileLoading = false,
  onClick,
  children,
  sx,
}) => {
  const s = sizeMap[size];
  const isDisabled = disabled || loading;

  const commonSx: MuiButtonProps['sx'] = {
    px: s.px,
    py: s.py,
    minHeight: s.minH,
    fontSize: s.fontSize,
    textTransform: 'uppercase',
    letterSpacing: '0.1em',
    fontWeight: 500,
    borderRadius: 0,
    transition: 'all 300ms cubic-bezier(0.4, 0, 0.2, 1)',
    whiteSpace: 'nowrap',
    lineHeight: 1,
    gap: '0.5rem',
    ...(variant === 'primary' && {
      bgcolor: '#000',
      color: '#fff',
      '&:hover': { bgcolor: '#1f2937' },
      '&:active': { bgcolor: '#111827' },
    }),
    ...(variant === 'outline' && {
      border: '1px solid #000',
      color: '#000',
      bgcolor: 'transparent',
      '&:hover': {
        bgcolor: '#000',
        color: '#fff',
        border: '1px solid #000',
      },
      '&:active': { bgcolor: '#111827', color: '#fff' },
    }),
    ...(variant === 'text' && {
      color: '#000',
      '&:hover': { opacity: 0.6, bgcolor: 'transparent' },
      '&:active': { opacity: 0.4 },
    }),
    ...(isDisabled && { opacity: 0.5, pointerEvents: 'none' }),
    ...sx,
  };

  const content = (
    <>
      {loading && <CircularProgress size={16} sx={{ color: 'inherit' }} />}
      <span
        style={{
          opacity: loading && !showContentWhileLoading ? 0 : 1,
        }}
      >
        {children}
      </span>
    </>
  );

  if (to) {
    return (
      <MuiButton
        component={Link}
        to={isDisabled ? '#' : to}
        sx={commonSx}
        fullWidth={fullWidth}
        aria-label={ariaLabel}
        aria-busy={loading}
        aria-disabled={isDisabled}
        tabIndex={isDisabled ? -1 : undefined}
      >
        {content}
      </MuiButton>
    );
  }

  if (href) {
    return (
      <MuiButton
        component="a"
        href={isDisabled ? undefined : href}
        target="_blank"
        rel="noopener noreferrer"
        sx={commonSx}
        fullWidth={fullWidth}
        aria-label={ariaLabel}
        aria-busy={loading}
        aria-disabled={isDisabled}
        tabIndex={isDisabled ? -1 : undefined}
      >
        {content}
      </MuiButton>
    );
  }

  return (
    <MuiButton
      type={type}
      disabled={isDisabled}
      onClick={onClick}
      sx={commonSx}
      fullWidth={fullWidth}
      aria-label={ariaLabel}
      aria-busy={loading}
    >
      {content}
    </MuiButton>
  );
};