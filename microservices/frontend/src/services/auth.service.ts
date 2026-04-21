import api from './api';
import type { LoginResponse, User } from '../models';

export const authService = {
  async login(username: string, password: string): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>('/auth/token', {
      username,
      password,
    });
    const data = response.data;
    localStorage.setItem('access_token', data.access_token);
    localStorage.setItem('refresh_token', data.refresh_token);
    return data;
  },

  logout(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
    window.location.href = '/login';
  },

  isLoggedIn(): boolean {
    return !!localStorage.getItem('access_token');
  },

  async getCurrentUser(): Promise<User> {
    const response = await api.get<User>('/auth/users/me');
    localStorage.setItem('user', JSON.stringify(response.data));
    return response.data;
  },

  getStoredUser(): User | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  async getUsers(): Promise<User[]> {
    const response = await api.get<User[]>('/auth/users');
    return response.data;
  },

  async getUser(id: string): Promise<User> {
    const response = await api.get<User>(`/auth/users/${id}`);
    return response.data;
  },

  async createUser(user: Partial<User> & { newPassword: string }): Promise<User> {
    const response = await api.post<User>('/auth/users', user);
    return response.data;
  },

  async updateUser(id: string, user: Partial<User>): Promise<void> {
    await api.put(`/auth/users/${id}`, user);
  },

  async deleteUser(id: string): Promise<void> {
    await api.delete(`/auth/users/${id}`);
  },
};
