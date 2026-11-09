import axios from 'axios';
import type { ProductDto, ProductDetail } from '../types/product.types';
import { productMapper } from '../mapper/productMapper';


/**
 * Product API service.
 *
 * Key behavior (mirrors Angular ProductService):
 * - getAllProducts() fetches once and caches raw DTOs in memory
 * - getProductDetailById() fetches per request (not cached) and maps to lang
 * - Cache can be invalidated via invalidateCache()
 *
 * NOTE: Once you run `npm run generate:api`, replace the manual axios
 * calls below with the generated ProductControllerService methods.
 * The mapper layer stays the same either way.
 */

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

class ProductService {
  /** In-memory cache of raw DTOs (fetched once, re-mapped per lang) */
  private cachedDtos: ProductDto[] | null = null;

  /**
   * Fetch all products (raw DTOs).
   * Caches the result so subsequent calls don't re-fetch.
   *
   * Angular equivalent:
   *   allDtos$ = this.api.getAllProducts().pipe(shareReplay(1))
   */
  async getAllProducts(activeOnly = false): Promise<ProductDto[]> {
    if (this.cachedDtos) return this.cachedDtos;

    try {
      const { data } = await api.get<ProductDto[]>('/products', {
        params: activeOnly ? { activeOnly: true } : undefined,
      });
      this.cachedDtos = data;
      return data;
    } catch (err) {
      console.error('[ProductService] Failed to fetch all products:', err);
      return [];
    }
  }

  /**
   * Fetch a single product by ID and map to ProductDetail.
   * Not cached — always fetches fresh (mirrors Angular behavior
   * where getProductDetailById re-fetches per lang change).
   *
   * Angular equivalent:
   *   getProductDetailById(id) → api.getProductById({ id })
   *     .pipe(map(dto => mapper.toProductDetail(dto, lang)))
   */
  async getProductDetailById(
    id: number,
    lang: string
  ): Promise<ProductDetail | null> {
    try {
      const { data } = await api.get<ProductDto>(`/products/${id}`);
      return productMapper.toProductDetail(data, lang);
    } catch (err) {
      console.error(
        `[ProductService] Failed to get product detail #${id}:`,
        err
      );
      return null;
    }
  }

  /**
   * Fetch a single product by ID and map to Product (not detail).
   */
  async getProductById(
    id: number,
    lang: string
  ): Promise<import('../types/product.types').Product | null> {
    try {
      const { data } = await api.get<ProductDto>(`/products/${id}`);
      return productMapper.toProduct(data, lang);
    } catch (err) {
      console.error(`[ProductService] Failed to get product #${id}:`, err);
      return null;
    }
  }

  /**
   * Fetch product by SKU.
   */
  async getProductBySku(
    sku: string,
    lang: string
  ): Promise<import('../types/product.types').Product | null> {
    try {
      const { data } = await api.get<ProductDto>(`/products/sku/${sku}`);
      return productMapper.toProduct(data, lang);
    } catch (err) {
      console.error(
        `[ProductService] Failed to get product by SKU '${sku}':`,
        err
      );
      return null;
    }
  }

  /**
   * Clear the in-memory DTO cache.
   * Call after admin creates/updates/deletes a product.
   */
  invalidateCache(): void {
    this.cachedDtos = null;
  }
}

// Singleton instance
export const productService = new ProductService();