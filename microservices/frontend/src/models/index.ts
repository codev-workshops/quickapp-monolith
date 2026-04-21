export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
}

export interface User {
  id: string;
  userName: string;
  fullName: string;
  email: string;
  jobTitle: string;
  phoneNumber: string;
  isEnabled: boolean;
  isLockedOut: boolean;
  roles: string[];
}

export interface UserEdit {
  id: string;
  userName: string;
  fullName: string;
  email: string;
  jobTitle: string;
  phoneNumber: string;
  isEnabled: boolean;
  currentPassword?: string;
  newPassword?: string;
  roles: string[];
}

export interface Role {
  id: string;
  name: string;
  description: string;
  usersCount: number;
  permissions: Permission[];
}

export interface Permission {
  name: string;
  value: string;
  groupName: string;
  description: string;
}

export interface Customer {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  address: string;
  city: string;
  gender: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  icon: string;
  buyingPrice: number;
  sellingPrice: number;
  unitsInStock: number;
  isActive: boolean;
  isDiscontinued: boolean;
  productCategoryId: number;
  productCategoryName: string;
}

export interface ProductCategory {
  id: number;
  name: string;
  description: string;
  icon: string;
}

export interface Order {
  id: number;
  discount: number;
  comments: string;
  cashierId: string;
  cashierName: string;
  customerId: number;
  customerName: string;
  orderDetails: OrderDetail[];
}

export interface OrderDetail {
  id: number;
  unitPrice: number;
  quantity: number;
  discount: number;
  productId: number;
  productName: string;
}

export interface TodoItem {
  id: number;
  name: string;
  completed: boolean;
}
