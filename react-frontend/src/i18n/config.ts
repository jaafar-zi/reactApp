import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';   
import HttpBackend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';
import { COOKIE_KEYS } from '../shared/constants/preferences.constants';

i18n
  .use(HttpBackend)
  .use(LanguageDetector)
  .use(initReactI18next)                             
  .init({
    fallbackLng: 'en',
    supportedLngs: ['en', 'fr', 'ar'],
    ns: ['common', 'products', 'auth'],
    defaultNS: 'common',
    backend: {
      loadPath: '/locales/{{lng}}/{{ns}}.json',
    },
    detection: {
      order: ['cookie', 'navigator'],
      caches: ['cookie'],
      lookupCookie: COOKIE_KEYS.lang,
    },
    interpolation: {
      escapeValue: false,
    },
  });

export default i18n;