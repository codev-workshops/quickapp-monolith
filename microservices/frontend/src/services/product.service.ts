import api from './api';
import type { Product, ProductCategory } from '../models';

export const productService = {
  async getAll(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products');
    return response.data;
  },

  async getById(id: number): Promise<Product> {
    const response = await api.get<Product>(`/products/${id}`);
    return response.data;
  },

  async create(product: Partial<Product>): Promise<Product> {
    const response = await api.post<Product>('/products', product);
    return response.data;
  },

  async update(id: number, product: Partial<Product>): Promise<Product> {
    const response = await api.put<Product>(`/products/${id}`, product);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/products/${id}`);
  },

  async getCategories(): Promise<ProductCategory[]> {
    const response = await api.get<ProductCategory[]>('/product-categories');
    return response.data;
  },

  async getCategoryById(id: number): Promise<ProductCategory> {
    const response = await api.get<ProductCategory>(`/product-categories/${id}`);
    return response.data;
  },

  async createCategory(category: Partial<ProductCategory>): Promise<ProductCategory> {
    const response = await api.post<ProductCategory>('/product-categories', category);
    return response.data;
  },

  async updateCategory(id: number, category: Partial<ProductCategory>): Promise<ProductCategory> {
    const response = await api.put<ProductCategory>(`/product-categories/${id}`, category);
    return response.data;
  },

  async deleteCategory(id: number): Promise<void> {
    await api.delete(`/product-categories/${id}`);
  },
};
