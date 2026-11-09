import React, { useEffect } from 'react';
import { initializePreferences } from '../features/preferences/slice/preferencesSlice';
import AppProviders from '../providers/AppProviders';
import { useAppDispatch } from '../store/hooks';
import AppRouter from './router';

const AppInner: React.FC = () => {
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(initializePreferences());
  }, [dispatch]);
  return <AppRouter />;
};

const App: React.FC = () => (
  <AppProviders>
    <AppInner />
  </AppProviders>
);

export default App;