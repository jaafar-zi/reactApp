import React, { useEffect, useState, useCallback, useRef } from 'react';
import { Box, TextField, MenuItem, InputAdornment } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { useTranslation } from 'react-i18next';
import { fetchProducts, selectProductListVm } from '../slice/productListSlice';
import {
  setCategory,
  setSortBy,
  setSearchQuery,
} from '../slice/filterSlice';
import { selectCurrency } from '../../preferences/slice/preferencesSlice';
import { ProductCard } from '../components/ProductCard';
import { CollectionHeader } from '../components/CollectionHeader';
import { SectionContainer } from '../../../shared/components/ui/SectionContainer';
import { EmptyState } from '../../../shared/components/ui/EmptyState';
import { Skeleton } from '../../../shared/components/ui/Skeleton';
import type { SortValue } from '../types/product.types';
import { PRODUCT_CATEGORIES } from '../../../shared/constants/product.constants';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';

export const ProductListPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation('products');
  const vm = useAppSelector(selectProductListVm);
  const currency = useAppSelector(selectCurrency);

  // Local search input (debounced before dispatching to Redux)
  const [searchInput, setSearchInput] = useState(vm.filter.searchQuery);
  const debounceRef = useRef<ReturnType<typeof setTimeout>>();

  // Fetch products on mount (mirrors Angular ProductListFacade.initialize())
  useEffect(() => {
    if (!vm.loading && vm.products.length === 0 && !vm.error) {
      dispatch(fetchProducts());
    }
  }, [dispatch, vm.loading, vm.products.length, vm.error]);

  // Debounced search (300ms, mirrors Angular FilterService.searchQuery$)
  const handleSearchChange = useCallback(
    (value: string) => {
      setSearchInput(value);
      if (debounceRef.current) clearTimeout(debounceRef.current);
      debounceRef.current = setTimeout(() => {
        dispatch(setSearchQuery(value));
      }, 300);
    },
    [dispatch]
  );

  useEffect(() => {
    return () => {
      if (debounceRef.current) clearTimeout(debounceRef.current);
    };
  }, []);

  const currencySymbol = currency === 'TND' ? 'TND' : currency;

  return (
    <Box
      sx={{
        pt: { xs: '8rem', sm: '10rem' },
        minHeight: '100vh',
      }}
    >
      {/* Header */}
      <CollectionHeader totalCount={vm.total} />

      {/* Filters Bar */}
      <SectionContainer padding="sm">
        <Box
          sx={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: '1rem',
            alignItems: 'center',
            justifyContent: 'space-between',
          }}
        >
          {/* Search */}
          <TextField
            size="small"
            placeholder={t('searchPlaceholder')}
            value={searchInput}
            onChange={e => handleSearchChange(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon sx={{ fontSize: '1rem', color: '#9ca3af' }} />
                </InputAdornment>
              ),
            }}
            sx={{
              minWidth: { xs: '100%', sm: '240px' },
              '& .MuiOutlinedInput-root': {
                borderRadius: 0,
                fontSize: '0.75rem',
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
              },
            }}
          />

          {/* Category + Sort */}
          <Box sx={{ display: 'flex', gap: '0.75rem' }}>
            <TextField
              select
              size="small"
              value={vm.filter.selectedCategory}
              onChange={e => dispatch(setCategory(e.target.value))}
              sx={{
                minWidth: '120px',
                '& .MuiOutlinedInput-root': { borderRadius: 0, fontSize: '0.75rem' },
              }}
            >
              <MenuItem value={PRODUCT_CATEGORIES.all}>{t('all')}</MenuItem>
              <MenuItem value={PRODUCT_CATEGORIES.caps}>{t('caps')}</MenuItem>
              <MenuItem value={PRODUCT_CATEGORIES.beanies}>{t('beanies')}</MenuItem>
            </TextField>

            <TextField
              select
              size="small"
              value={vm.filter.sortBy}
              onChange={e => dispatch(setSortBy(e.target.value as SortValue))}
              sx={{
                minWidth: '140px',
                '& .MuiOutlinedInput-root': { borderRadius: 0, fontSize: '0.75rem' },
              }}
            >
              <MenuItem value="featured">{t('sortFeatured')}</MenuItem>
              <MenuItem value="price-low">{t('sortPriceLow')}</MenuItem>
              <MenuItem value="price-high">{t('sortPriceHigh')}</MenuItem>
              <MenuItem value="name-asc">{t('sortNameAsc')}</MenuItem>
              <MenuItem value="name-desc">{t('sortNameDesc')}</MenuItem>
            </TextField>
          </Box>
        </Box>
      </SectionContainer>

      {/* Product Grid */}
      <SectionContainer padding="md">
        {/* Loading State — 9 skeleton cards */}
        {vm.loading && (
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: { xs: 'repeat(2, 1fr)', md: 'repeat(3, 1fr)' },
              gap: { xs: '1rem', md: '2rem' },
              animation: 'fadeInUp 0.5s ease',
              '@keyframes fadeInUp': {
                from: { opacity: 0, transform: 'translateY(10px)' },
                to: { opacity: 1, transform: 'translateY(0)' },
              },
            }}
          >
            {Array.from({ length: 9 }).map((_, i) => (
              <Box key={i}>
                <Skeleton variant="square" />
                <Box sx={{ mt: '0.75rem' }}>
                  <Skeleton variant="text" width="60%" height="0.75rem" />
                  <Box sx={{ mt: '0.5rem' }}>
                    <Skeleton variant="text" width="30%" height="0.75rem" />
                  </Box>
                </Box>
              </Box>
            ))}
          </Box>
        )}

        {/* Empty State */}
        {vm.isEmpty && (
          <EmptyState
            title={t('noProductsTitle')}
            subtitle={t('noProductsSubtitle')}
            ctaLabel={t('clearFilters')}
            onCtaClick={() => {
              dispatch(setCategory('all'));
              dispatch(setSortBy('featured'));
              dispatch(setSearchQuery(''));
              setSearchInput('');
            }}
          />
        )}

        {/* Product Grid */}
        {!vm.loading && vm.hasResults && (
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: { xs: 'repeat(2, 1fr)', md: 'repeat(3, 1fr)' },
              gap: { xs: '1rem', md: '2rem' },
              animation: 'fadeInUp 0.5s ease',
              '@keyframes fadeInUp': {
                from: { opacity: 0, transform: 'translateY(10px)' },
                to: { opacity: 1, transform: 'translateY(0)' },
              },
            }}
          >
            {vm.products.map(product => (
              <ProductCard
                key={product.id}
                product={product}
                currencySymbol={currencySymbol}
              />
            ))}
          </Box>
        )}

        {/* Error State */}
        {vm.error && (
          <EmptyState
            title={t('errorTitle')}
            subtitle={vm.error}
            ctaLabel={t('retry')}
            onCtaClick={() => dispatch(fetchProducts())}
          />
        )}
      </SectionContainer>
    </Box>
  );
};