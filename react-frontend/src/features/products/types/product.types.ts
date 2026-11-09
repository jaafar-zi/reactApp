export interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  hoverImage?: string;
  images: string[];
  description: string;
  category: string;
  inStock: boolean;
  sku: string;
  sizes: string[];
  colors: string[];
  features: string[];
  attributes: ProductAttribute[];
  slug: string;
}

export interface ProductDetail extends Product {
  mainImage: string;
  lifestyleImages: string[];
  garmentDetails: string;
  washCare: string;
  collection: string;
  origin: string;
  edition: string;
}

export interface ProductAttribute {
  id: string;
  title: string;
  content: string;
}

export interface ProductVariant {
  sku: string;
  size?: string;
  color?: string;
  price?: number;
  inStock: boolean;
}

export interface ProductSelection {
  size: string | null;
  color: string | null;
  quantity: number;
}

// DTO types (matches Swagger-generated ProductDto)
export interface ProductDto {
  id?: number;
  name?: string;
  description?: string;
  sku?: string;
  active?: boolean;
  pricing?: { basePrice?: number; salePrice?: number };
  inventory?: { inStock?: boolean; quantity?: number };
  assets?: ProductAssetDto[];
  attributes?: ProductAttributeDto[];
}

export interface ProductAssetDto {
  url?: string;
  isPrimary?: boolean;
  alt?: string;
  type?: string;
}

export interface ProductAttributeDto {
  attributeKey?: string;
  language?: string;
  attributeValues?: { value?: string }[];
}

export type SortValue =
  | 'featured'
  | 'price-low'
  | 'price-high'
  | 'name'
  | 'name-asc'
  | 'name-desc';

export interface FilterState {
  selectedCategory: string;
  sortBy: SortValue;
  searchQuery: string;
}