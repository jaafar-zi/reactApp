export type SupportedLang = 'en' | 'fr' | 'ar';
export type SupportedCurrency = 'TND';
export type LayoutDirection = 'ltr' | 'rtl';

export interface LangConfig {
  code: SupportedLang;
  label: string;
  nativeLabel: string;
  currency: SupportedCurrency;
  direction: LayoutDirection;
  flag: string;
}

export const LANG_CONFIGS: Record<SupportedLang, LangConfig> = {
  en: { code: 'en', label: 'English', nativeLabel: 'English',  currency: 'TND', direction: 'ltr', flag: '🇬🇧' },
  fr: { code: 'fr', label: 'French',  nativeLabel: 'Français', currency: 'TND', direction: 'ltr', flag: '🇫🇷' },
  ar: { code: 'ar', label: 'Arabic',  nativeLabel: 'العربية',   currency: 'TND', direction: 'rtl', flag: '🇹🇳' },
};

export const DEFAULT_LANG: SupportedLang = 'en';
export const DEFAULT_CURRENCY: SupportedCurrency = 'TND';

export const COOKIE_KEYS = {
  lang: 'JAWISTA_LANG',
  currency: 'JAWISTA_CURRENCY',
} as const;

export const SUPPORTED_LANGS: SupportedLang[] = ['en', 'fr', 'ar'];
export const SUPPORTED_CURRENCIES: SupportedCurrency[] = ['TND'];