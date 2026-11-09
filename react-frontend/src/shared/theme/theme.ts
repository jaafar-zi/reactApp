import { createTheme, type ThemeOptions } from '@mui/material/styles';

export const SPACING = {
  xs: 0.5, s: 0.625, sm: 1, m: 1.25, md: 1.5,
  lg: 2, xl: 3, '2xl': 4, '3xl': 5, '4xl': 6,
} as const;

export const XS_BREAKPOINT = 480;

export const createAppTheme = (direction: 'ltr' | 'rtl' = 'ltr') => {
  const options: ThemeOptions = {
    direction,
    spacing: (factor: number) => `${factor * 8}px`,
    breakpoints: {
      values: { xs: 0, sm: 768, md: 1024, lg: 1280, xl: 1440 },
    },
    palette: {
      common: { black: '#000000', white: '#ffffff' },
      primary: { main: '#000000' },
      secondary: { main: '#111827' },
      background: { default: '#ffffff', paper: '#ffffff' },
      text: {
        primary: '#111827',
        secondary: '#6b7280',
        disabled: '#9ca3af',
      },
      grey: {
        50: '#f9fafb', 100: '#f3f4f6', 200: '#e5e7eb',
        300: '#d1d5db', 400: '#9ca3af', 500: '#6b7280',
        600: '#4b5563', 700: '#374151', 800: '#1f2937',
        900: '#111827',
      },
      error: { main: '#ef4444' },
      success: { main: '#22c55e' },
      warning: { main: '#facc15' },
      divider: '#e5e7eb',
    },
    typography: {
      fontFamily: "'DM Sans', sans-serif",
      fontSize: 12,
      fontWeightLight: 400,
      fontWeightRegular: 400,
      fontWeightMedium: 500,
      fontWeightBold: 700,
      h1: { fontSize: 'clamp(2rem, 5vw, 3.5rem)', fontWeight: 700, lineHeight: 1.1 },
      h2: { fontSize: 'clamp(1.5rem, 3vw, 2.5rem)', fontWeight: 700, lineHeight: 1.2 },
      h3: { fontSize: 'clamp(1.25rem, 2vw, 1.75rem)', fontWeight: 600, lineHeight: 1.3 },
      h4: { fontSize: '1.5rem', fontWeight: 600 },
      h5: { fontSize: '1.25rem', fontWeight: 600 },
      h6: { fontSize: '1rem', fontWeight: 600 },
      body1: { fontSize: '1rem', lineHeight: 1.6 },
      body2: { fontSize: '0.875rem', lineHeight: 1.5 },
      caption: { fontSize: '0.75rem', lineHeight: 1.4 },
      button: { textTransform: 'none', fontWeight: 600 },
    },
    shape: { borderRadius: 4 },
    transitions: {
      easing: {
        easeInOut: 'cubic-bezier(0.4, 0, 0.2, 1)',
        easeOut: 'cubic-bezier(0.0, 0, 0.2, 1)',
        easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
      },
      duration: {
        shortest: 150, shorter: 200, short: 300,
        standard: 600, complex: 1000,
      },
    },
    components: {
      MuiCssBaseline: {
        styleOverrides: {
          body: {
            fontFamily: "'DM Sans', sans-serif",
            WebkitFontSmoothing: 'antialiased',
            MozOsxFontSmoothing: 'grayscale',
          },
          '*': {
            '&::-webkit-scrollbar': { width: 6 },
            '&::-webkit-scrollbar-thumb': {
              backgroundColor: '#888', borderRadius: 3,
            },
          },
        },
      },
      MuiButton: {
        styleOverrides: {
          root: {
            borderRadius: 0,
            textTransform: 'uppercase',
            letterSpacing: '0.05em',
            fontWeight: 600,
          },
        },
      },
      MuiCard: {
        styleOverrides: {
          root: { borderRadius: 0, boxShadow: 'none' },
        },
      },
    },
  };

  return createTheme(options);
};