import api from './api';
import type {
  LoginRequest,
  RegisterRequest,
  AuthResponse,
  ProfileResponse,
  UpdateProfileRequest,
  ChangePasswordRequest,
} from '../types/auth.types';

export const authService = {
  login: (data: LoginRequest) =>
    api.post<AuthResponse>('/api/auth/login', data).then((r) => r.data),

  register: (data: RegisterRequest) =>
    api.post<AuthResponse>('/api/auth/register', data).then((r) => r.data),

  getProfile: () =>
    api.get<ProfileResponse>('/api/auth/profile').then((r) => r.data),

  updateProfile: (data: UpdateProfileRequest) =>
    api.put<ProfileResponse>('/api/auth/profile', data).then((r) => r.data),

  changePassword: (data: ChangePasswordRequest) =>
    api.patch('/api/auth/profile/password', data).then((r) => r.data),
};