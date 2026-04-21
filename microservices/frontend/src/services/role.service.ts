import api from './api';
import type { Role, Permission } from '../models';

export const roleService = {
  async getAll(): Promise<Role[]> {
    const response = await api.get<Role[]>('/auth/roles');
    return response.data;
  },

  async getById(id: string): Promise<Role> {
    const response = await api.get<Role>(`/auth/roles/${id}`);
    return response.data;
  },

  async create(role: Partial<Role>): Promise<Role> {
    const response = await api.post<Role>('/auth/roles', role);
    return response.data;
  },

  async update(id: string, role: Partial<Role>): Promise<Role> {
    const response = await api.put<Role>(`/auth/roles/${id}`, role);
    return response.data;
  },

  async delete(id: string): Promise<void> {
    await api.delete(`/auth/roles/${id}`);
  },

  async getPermissions(): Promise<Permission[]> {
    const response = await api.get<Permission[]>('/auth/permissions');
    return response.data;
  },
};
