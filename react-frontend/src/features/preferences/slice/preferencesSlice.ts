import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { PreferencesState } from '../types/preferences.types';
import {
  type SupportedLang,
  type SupportedCurrency,
  DEFAULT_LANG,
  DEFAULT_CURRENCY,
  SUPPORTED_LANGS,
  SUPPORTED_CURRENCIES,
  COOKIE_KEYS,
  LANG_CONFIGS,
} from '../../../shared/constants/preferences.constants';
import { cookie } from '../../../shared/utils/cookie';

// ── Resolve initial values (mirrors Angular PreferencesService) ──

function resolveInitialLang(): SupportedLang {
  // 1. Cookie
  const cookieLang = cookie.get(COOKIE_KEYS.lang);
  if (cookieLang && SUPPORTED_LANGS.includes(cookieLang as SupportedLang)) {
    return cookieLang as SupportedLang;
  }
  // 2. Browser language
  if (typeof navigator !== 'undefined') {
    const browserLang = navigator.language?.split('-')[0];
    if (browserLang && SUPPORTED_LANGS.includes(browserLang as SupportedLang)) {
      return browserLang as SupportedLang;
    }
  }
  // 3. Default
  return DEFAULT_LANG;
}

function resolveInitialCurrency(): SupportedCurrency {
  const cookieCurrency = cookie.get(COOKIE_KEYS.currency);
  if (
    cookieCurrency &&
    SUPPORTED_CURRENCIES.includes(cookieCurrency as SupportedCurrency)
  ) {
    return cookieCurrency as SupportedCurrency;
  }
  return DEFAULT_CURRENCY;
}

// ── Apply language to DOM (mirrors Angular applyLang) ──

function applyLangToDom(lang: SupportedLang): void {
  if (typeof document === 'undefined') return;
  const config = LANG_CONFIGS[lang];
  document.documentElement.setAttribute('lang', lang);
  document.documentElement.setAttribute('dir', config.direction);
}

// ── Slice ──

const initialState: PreferencesState = {
  lang: resolveInitialLang(),
  currency: resolveInitialCurrency(),
};

const preferencesSlice = createSlice({
  name: 'preferences',
  initialState,
  reducers: {
    /**
     * Switch language.
     * - Persists to cookie (JAWISTA_LANG)
     * - Updates DOM lang + dir attributes (RTL for Arabic)
     * - Auto-sets currency from lang config if user hasn't changed it
     */
    setLang(state, action: PayloadAction<SupportedLang>) {
      const lang = action.payload;
      if (!SUPPORTED_LANGS.includes(lang)) return;

      state.lang = lang;
      cookie.set(COOKIE_KEYS.lang, lang);
      applyLangToDom(lang);

      // Auto-set currency based on lang if user hasn't manually changed it
      const langCurrency = LANG_CONFIGS[lang].currency;
      if (state.currency === resolveInitialCurrency()) {
        state.currency = langCurrency;
        cookie.set(COOKIE_KEYS.currency, langCurrency);
      }
    },

    /**
     * Switch currency.
     * - Persists to cookie (JAWISTA_CURRENCY)
     */
    setCurrency(state, action: PayloadAction<SupportedCurrency>) {
      if (!SUPPORTED_CURRENCIES.includes(action.payload)) return;
      state.currency = action.payload;
      cookie.set(COOKIE_KEYS.currency, action.payload);
    },

    /**
     * Call once on app mount to apply initial lang to DOM.
     * Mirrors Angular PreferencesService.initialize().
     */
    initializePreferences(state) {
      applyLangToDom(state.lang);
    },
  },
});

export const { setLang, setCurrency, initializePreferences } =
  preferencesSlice.actions;

// ── Selectors ──

export const selectLang = (state: { preferences: PreferencesState }) =>
  state.preferences.lang;

export const selectCurrency = (state: { preferences: PreferencesState }) =>
  state.preferences.currency;

export const selectDirection = (state: { preferences: PreferencesState }) =>
  LANG_CONFIGS[state.preferences.lang].direction;

export default preferencesSlice.reducer;