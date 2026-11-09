import type {
  SupportedLang,
  SupportedCurrency,
} from '../../../shared/constants/preferences.constants';

export interface PreferencesState {
  lang: SupportedLang;
  currency: SupportedCurrency;
}