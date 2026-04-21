import api from './api';
import type { Order } from '../models';

export const orderService = {
  async getAll(): Promise<Order[]> {
    const response = await api.get<Order[]>('/orders');
    return response.data;
  },

  async getById(id: number): Promise<Order> {
    const response = await api.get<Order>(`/orders/${id}`);
    return response.data;
  },

  async create(order: Partial<Order>): Promise<Order> {
    const response = await api.post<Order>('/orders', order);
    return response.data;
  },

  async update(id: number, order: Partial<Order>): Promise<Order> {
    const response = await api.put<Order>(`/orders/${id}`, order);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/orders/${id}`);
  },
};
