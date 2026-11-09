import React from 'react';
import { Box, Typography } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { Button } from './Button';

interface EmptyStateProps {
  title?: string;
  subtitle?: string;
  ctaLabel?: string;
  showCta?: boolean;
  onCtaClick?: () => void;
  icon?: React.ReactNode;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  title,
  subtitle,
  ctaLabel,
  showCta = true,
  onCtaClick,
  icon,
}) => (
  <Box
    sx={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      textAlign: 'center',
      padding: '6rem 2rem',
    }}
  >
    {/* Icon slot — default is SearchIcon lg */}
    <Box sx={{ mb: '1.5rem', color: '#d1d5db' }}>
      {icon ?? <SearchIcon sx={{ width: '4rem', height: '4rem' }} />}
    </Box>

    {title && (
      <Typography sx={{ fontSize: '0.875rem', color: '#6b7280', mb: '0.5rem' }}>
        {title}
      </Typography>
    )}

    {subtitle && (
      <Typography sx={{ fontSize: '0.75rem', color: '#9ca3af', mb: '1.5rem' }}>
        {subtitle}
      </Typography>
    )}

    {showCta && ctaLabel && (
      <Box sx={{ mt: '1rem' }}>
        <Button variant="outline" onClick={onCtaClick}>
          {ctaLabel}
        </Button>
      </Box>
    )}
  </Box>
);