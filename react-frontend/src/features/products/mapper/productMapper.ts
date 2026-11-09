import type {
  Product,
  ProductDetail,
  ProductAttribute,
  ProductDto,
  ProductAssetDto,
  ProductAttributeDto,
} from '../types/product.types';

/**
 * Maps raw Swagger-generated DTOs → domain Product/ProductDetail models.
 *
 * Key behavior (mirrors Angular ProductMapper exactly):
 * - Multilingual attribute resolution: exact lang → en fallback → no-lang → first
 * - Sizes/colors/features extracted from comma-separated attribute values
 * - Wash care labels hardcoded in French (Lavage/Séchage/Repassage)
 * - Reserved attribute keys excluded from generic attributes list
 */
class ProductMapper {
  // ── Public API ────────────────────────────────────────────────

  toProduct(dto: ProductDto, lang: string): Product {
    const assets = dto.assets ?? [];
    const pricing = dto.pricing;
    const inventory = dto.inventory;

    const primaryAsset = this.getPrimaryAsset(assets);
    const secondaryAssets = this.getSecondaryAssets(assets);

    return {
      id: dto.id ?? 0,
      name: dto.name ?? '',
      price: pricing?.salePrice ?? pricing?.basePrice ?? 0,
      image: primaryAsset?.url ?? 'assets/images/placeholder-product.jpg',
      hoverImage: secondaryAssets[0]?.url,
      images: assets.map(a => a.url ?? '').filter(Boolean),
      description: dto.description ?? '',
      category: this.extractCategory(dto, lang),
      inStock: inventory?.inStock ?? false,
      sku: dto.sku ?? '',
      sizes: this.extractSizes(dto, lang),
      colors: this.extractColors(dto, lang),
      features: this.extractFeatures(dto, lang),
      attributes: this.extractAttributes(dto, lang),
      slug: this.buildSlug(dto.name ?? ''),
    };
  }

  toProductList(dtos: ProductDto[], lang: string): Product[] {
    return dtos.map(dto => this.toProduct(dto, lang));
  }

  toProductDetail(dto: ProductDto, lang: string): ProductDetail {
    const product = this.toProduct(dto, lang);
    const assets = dto.assets ?? [];

    return {
      ...product,
      mainImage: product.image,
      lifestyleImages: this.getSecondaryAssets(assets)
        .map(a => a.url ?? '')
        .filter(Boolean),
      garmentDetails: this.extractAttributeValues(dto, 'material', lang),
      washCare: this.extractWashCare(dto, lang),
      collection: this.extractFirstValue(dto, 'collection', lang),
      origin: this.extractFirstValue(dto, 'origin', lang),
      edition: this.extractFirstValue(dto, 'edition', lang),
    };
  }

  // ── Asset Helpers ─────────────────────────────────────────────

  private getPrimaryAsset(
    assets: ProductAssetDto[]
  ): ProductAssetDto | undefined {
    return assets.find(a => a.isPrimary) ?? assets[0];
  }

  private getSecondaryAssets(assets: ProductAssetDto[]): ProductAssetDto[] {
    const primary = this.getPrimaryAsset(assets);
    return assets.filter(a => a !== primary);
  }

  // ── Attribute Resolution ──────────────────────────────────────
  // Priority: exact lang → English fallback → no-lang → first available

  private findAttribute(
    dto: ProductDto,
    key: string,
    lang: string
  ): ProductAttributeDto | undefined {
    const attrs = (dto.attributes ?? []).filter(
      a => a.attributeKey === key
    );

    // 1. Exact language match
    const exact = attrs.find(a => a.language === lang);
    if (exact) return exact;

    // 2. English fallback
    const enFallback = attrs.find(a => a.language === 'en');
    if (enFallback) return enFallback;

    // 3. No language field (language-agnostic)
    const noLang = attrs.find(a => !a.language);
    if (noLang) return noLang;

    // 4. Any available
    return attrs[0];
  }

  private extractFirstValue(
    dto: ProductDto,
    key: string,
    lang: string
  ): string {
    const attr = this.findAttribute(dto, key, lang);
    return attr?.attributeValues?.[0]?.value ?? '';
  }

  private extractAllValues(
    dto: ProductDto,
    key: string,
    lang: string
  ): string[] {
    const attr = this.findAttribute(dto, key, lang);
    return (attr?.attributeValues ?? [])
      .map(v => v.value ?? '')
      .filter(Boolean);
  }

  // ── Specific Extractors ───────────────────────────────────────

  private extractCategory(dto: ProductDto, lang: string): string {
    return (
      this.extractFirstValue(dto, 'category', lang) ||
      this.extractFirstValue(dto, 'type', lang) ||
      'uncategorized'
    );
  }

  private extractSizes(dto: ProductDto, lang: string): string[] {
    return this.extractAllValues(dto, 'sizes', lang)
      .flatMap(v => v.split(',').map(s => s.trim()))
      .filter(Boolean);
  }

  private extractColors(dto: ProductDto, lang: string): string[] {
    return this.extractAllValues(dto, 'colors', lang)
      .flatMap(v => v.split(',').map(c => c.trim()))
      .filter(Boolean);
  }

  private extractFeatures(dto: ProductDto, lang: string): string[] {
    return this.extractAllValues(dto, 'features', lang)
      .flatMap(v => v.split(',').map(f => f.trim()))
      .filter(Boolean);
  }

  private extractAttributeValues(
    dto: ProductDto,
    key: string,
    lang: string
  ): string {
    return this.extractAllValues(dto, key, lang).join('\n');
  }

  /**
   * Wash care instructions.
   * NOTE: Labels are hardcoded in French in the Angular source.
   * Consider moving these to i18n translations in a future iteration.
   */
  private extractWashCare(dto: ProductDto, lang: string): string {
    const wash = this.extractAllValues(dto, 'wash_instructions', lang);
    const dry = this.extractAllValues(dto, 'dry_instructions', lang);
    const iron = this.extractAllValues(dto, 'iron_instructions', lang);
    const dryClean = this.extractFirstValue(dto, 'dry_clean', lang);

    return [
      wash.length ? `Lavage:\n${wash.join('\n')}` : '',
      dry.length ? `Séchage:\n${dry.join('\n')}` : '',
      iron.length ? `Repassage:\n${iron.join('\n')}` : '',
      dryClean,
    ]
      .filter(Boolean)
      .join('\n\n');
  }

  /**
   * Extract all non-reserved attributes for the accordion display.
   * Groups by key, picks best language match per key.
   */
  private extractAttributes(
    dto: ProductDto,
    lang: string
  ): ProductAttribute[] {
    const reserved = new Set([
      'category',
      'type',
      'sizes',
      'size',
      'colors',
      'color',
      'features',
      'name',
      'description',
      'wash_instructions',
      'dry_instructions',
      'iron_instructions',
      'dry_clean',
      'collection',
      'origin',
      'edition',
    ]);

    const allAttrs = dto.attributes ?? [];
    const byKey = new Map<string, ProductAttributeDto>();

    for (const attr of allAttrs) {
      const key = attr.attributeKey;
      if (!key || reserved.has(key)) continue;

      const existing = byKey.get(key);

      if (!existing) {
        byKey.set(key, attr);
        continue;
      }

      // Prefer exact language match
      if (attr.language === lang) {
        byKey.set(key, attr);
        continue;
      }

      // Prefer English over unknown
      if (attr.language === 'en' && existing.language !== lang) {
        byKey.set(key, attr);
      }
    }

    return Array.from(byKey.values()).map(a => ({
      id: a.attributeKey ?? '',
      title: this.formatAttributeTitle(a.attributeKey ?? ''),
      content: (a.attributeValues ?? [])
        .map(v => v.value ?? '')
        .filter(Boolean)
        .join(', '),
    }));
  }

  // ── String Helpers ────────────────────────────────────────────

  private buildSlug(name: string): string {
    return name
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '');
  }

  private formatAttributeTitle(key: string): string {
    return key
      .replace(/-/g, ' ')
      .replace(/_/g, ' ')
      .replace(/\b\w/g, c => c.toUpperCase());
  }
}

// Singleton instance (mirrors Angular's @Injectable({ providedIn: 'root' }))
export const productMapper = new ProductMapper();