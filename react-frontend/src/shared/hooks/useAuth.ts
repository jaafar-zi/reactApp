import { useState, useCallback, useEffect } from 'react';
import api from '../../features/auth/services/api';
import type { ProfileResponse } from '../../features/auth/types/auth.types';

const useAuth = () => {
  const [user, setUser] = useState<ProfileResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const token = localStorage.getItem('token');
  const isAuthenticated = !!token;

  const refreshProfile = useCallback(async () => {
    if (!isAuthenticated) {
      setLoading(false);
      return;
    }
    try {
      const { data } = await api.get<ProfileResponse>('/api/auth/profile');
      setUser(data);
    } catch {
      localStorage.removeItem('token');
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]);

  useEffect(() => {
    refreshProfile();
  }, [refreshProfile]);

  const login = useCallback(async (email: string, password: string) => {
    setError(null);
    try {
      const { data } = await api.post('/api/auth/login', { email, password });
      localStorage.setItem('token', data.token);
      await refreshProfile();
      return data;
    } catch (err: any) {
      const msg = err?.response?.data?.message ?? 'Login failed';
      setError(msg);
      throw err;
    }
  }, [refreshProfile]);

  const register = useCallback(async (payload: {
    email: string; password: string; firstName: string; lastName: string;
  }) => {
    setError(null);
    try {
      const { data } = await api.post('/api/auth/register', payload);
      localStorage.setItem('token', data.token);
      await refreshProfile();
      return data;
    } catch (err: any) {
      const msg = err?.response?.data?.message ?? 'Registration failed';
      setError(msg);
      throw err;
    }
  }, [refreshProfile]);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  }, []);

  return {
    user,
    loading,
    error,
    isAuthenticated,
    login,
    register,
    logout,
    refreshProfile,
  };
};

export default useAuth;