import React from 'react';
import { Box, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { SectionContainer } from '../../../shared/components/ui/SectionContainer';
import { SectionTitle } from '../../../shared/components/ui/SectionTitle';

interface CollectionHeaderProps {
  totalCount: number;
  collectionName?: string;
}

export const CollectionHeader: React.FC<CollectionHeaderProps> = ({
  totalCount,
  collectionName,
}) => {
  const { t } = useTranslation('products');
  const year = new Date().getFullYear();

  return (
    <SectionContainer padding="sm">
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: { xs: '1fr', sm: '1fr auto 1fr' },
          alignItems: 'center',
          gap: '1rem',
        }}
      >
        {/* Left — Label */}
        <Typography
          sx={{
            fontSize: '0.75rem',
            textTransform: 'uppercase',
            letterSpacing: '0.15em',
            color: '#6b7280',
            textAlign: { xs: 'center', sm: 'left' },
          }}
        >
          {t('collection')}
        </Typography>

        {/* Center — Collection Name + Copyright */}
        <Box sx={{ textAlign: 'center' }}>
          <SectionTitle level="h2" size="section" weight="bold">
            {collectionName ?? t('allProducts')}
          </SectionTitle>
          <Typography
            sx={{
              fontSize: '0.625rem',
              color: '#9ca3af',
              mt: '0.25rem',
              letterSpacing: '0.1em',
              textTransform: 'uppercase',
            }}
          >
            ORGC ©{year}
          </Typography>
        </Box>

        {/* Right — Count */}
        <Typography
          sx={{
            fontSize: '0.75rem',
            color: '#6b7280',
            textAlign: { xs: 'center', sm: 'right' },
            letterSpacing: '0.1em',
          }}
        >
          {totalCount} {t('items')}
        </Typography>
      </Box>
    </SectionContainer>
  );
};