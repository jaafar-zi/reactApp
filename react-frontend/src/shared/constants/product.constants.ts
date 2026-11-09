export const PRODUCT_LIMITS = {
  featured: 4,
  newArrivals: 8,
  related: 3,
} as const;

export const PRODUCT_CATEGORIES = ['all', 'caps', 'beanies'] as const;

export const PRODUCT_SORT_OPTIONS = [
  { value: 'featured', label: 'Featured' },
  { value: 'price-low', label: 'Price: Low to High' },
  { value: 'price-high', label: 'Price: High to Low' },
  { value: 'name', label: 'Name' },
] as const;