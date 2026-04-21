import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Modal, Form, Alert, Spinner } from 'react-bootstrap';
import { productService } from '../services/product.service';
import type { Product, ProductCategory } from '../models';

const ProductsPage: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<ProductCategory[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<Product | null>(null);
  const [form, setForm] = useState({ name: '', description: '', buyingPrice: 0, sellingPrice: 0, unitsInStock: 0, productCategoryId: 0, isActive: true });

  const loadData = async () => {
    try {
      setLoading(true);
      const [prods, cats] = await Promise.all([productService.getAll(), productService.getCategories()]);
      setProducts(prods);
      setCategories(cats);
    } catch {
      setError('Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadData(); }, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', description: '', buyingPrice: 0, sellingPrice: 0, unitsInStock: 0, productCategoryId: categories[0]?.id || 0, isActive: true });
    setShowModal(true);
  };

  const openEdit = (p: Product) => {
    setEditing(p);
    setForm({ name: p.name, description: p.description || '', buyingPrice: p.buyingPrice, sellingPrice: p.sellingPrice, unitsInStock: p.unitsInStock, productCategoryId: p.productCategoryId, isActive: p.isActive });
    setShowModal(true);
  };

  const handleSave = async () => {
    try {
      if (editing) {
        await productService.update(editing.id, form);
      } else {
        await productService.create(form);
      }
      setShowModal(false);
      loadData();
    } catch {
      setError('Failed to save product');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Delete this product?')) return;
    try {
      await productService.delete(id);
      loadData();
    } catch {
      setError('Failed to delete product');
    }
  };

  if (loading) return <Container className="mt-4 text-center"><Spinner animation="border" /></Container>;

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center page-header">
        <h2>Products</h2>
        <Button variant="primary" onClick={openCreate}>New Product</Button>
      </div>
      {error && <Alert variant="danger" dismissible onClose={() => setError('')}>{error}</Alert>}
      <Table striped bordered hover responsive>
        <thead>
          <tr><th>#</th><th>Name</th><th>Category</th><th>Buying Price</th><th>Selling Price</th><th>Stock</th><th>Active</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {products.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td><td>{p.name}</td><td>{p.productCategoryName}</td>
              <td>${p.buyingPrice?.toLocaleString()}</td><td>${p.sellingPrice?.toLocaleString()}</td>
              <td>{p.unitsInStock}</td><td>{p.isActive ? 'Yes' : 'No'}</td>
              <td>
                <Button variant="outline-primary" size="sm" className="me-1" onClick={() => openEdit(p)}>Edit</Button>
                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(p.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton><Modal.Title>{editing ? 'Edit Product' : 'New Product'}</Modal.Title></Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3"><Form.Label>Name</Form.Label><Form.Control value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Description</Form.Label><Form.Control as="textarea" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Category</Form.Label>
              <Form.Select value={form.productCategoryId} onChange={e => setForm({ ...form, productCategoryId: Number(e.target.value) })}>
                {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3"><Form.Label>Buying Price</Form.Label><Form.Control type="number" value={form.buyingPrice} onChange={e => setForm({ ...form, buyingPrice: Number(e.target.value) })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Selling Price</Form.Label><Form.Control type="number" value={form.sellingPrice} onChange={e => setForm({ ...form, sellingPrice: Number(e.target.value) })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Units in Stock</Form.Label><Form.Control type="number" value={form.unitsInStock} onChange={e => setForm({ ...form, unitsInStock: Number(e.target.value) })} /></Form.Group>
            <Form.Check className="mb-3" type="checkbox" label="Active" checked={form.isActive} onChange={e => setForm({ ...form, isActive: e.target.checked })} />
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
          <Button variant="primary" onClick={handleSave}>Save</Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default ProductsPage;
