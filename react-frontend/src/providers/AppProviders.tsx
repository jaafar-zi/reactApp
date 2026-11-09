import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider, CssBaseline } from '@mui/material';
import { createAppTheme } from '../shared/theme/theme';
import { useAppSelector } from '../store/hooks';
import { store } from '../store/store';

const ThemeWrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const direction = useAppSelector((s) =>
    s.preferences.lang === 'ar' ? 'rtl' : 'ltr'
  );
  const theme = React.useMemo(() => createAppTheme(direction), [direction]);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      {children}
    </ThemeProvider>
  );
};

const AppProviders: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <Provider store={store}>
    <BrowserRouter>
      <ThemeWrapper>{children}</ThemeWrapper>
    </BrowserRouter>
  </Provider>
);

export default AppProviders;