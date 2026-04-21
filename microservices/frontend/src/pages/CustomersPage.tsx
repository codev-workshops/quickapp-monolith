import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Modal, Form, Alert, Spinner } from 'react-bootstrap';
import { customerService } from '../services/customer.service';
import type { Customer } from '../models';

const CustomersPage: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<Customer | null>(null);
  const [form, setForm] = useState({ name: '', email: '', phoneNumber: '', address: '', city: '', gender: 'Male' });

  const loadCustomers = async () => {
    try {
      setLoading(true);
      const data = await customerService.getAll();
      setCustomers(data);
    } catch {
      setError('Failed to load customers');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadCustomers(); }, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', email: '', phoneNumber: '', address: '', city: '', gender: 'Male' });
    setShowModal(true);
  };

  const openEdit = (c: Customer) => {
    setEditing(c);
    setForm({ name: c.name, email: c.email, phoneNumber: c.phoneNumber || '', address: c.address || '', city: c.city || '', gender: c.gender || 'Male' });
    setShowModal(true);
  };

  const handleSave = async () => {
    try {
      if (editing) {
        await customerService.update(editing.id, form);
      } else {
        await customerService.create(form);
      }
      setShowModal(false);
      loadCustomers();
    } catch {
      setError('Failed to save customer');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Delete this customer?')) return;
    try {
      await customerService.delete(id);
      loadCustomers();
    } catch {
      setError('Failed to delete customer');
    }
  };

  if (loading) return <Container className="mt-4 text-center"><Spinner animation="border" /></Container>;

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center page-header">
        <h2>Customers</h2>
        <Button variant="primary" onClick={openCreate}>New Customer</Button>
      </div>
      {error && <Alert variant="danger" dismissible onClose={() => setError('')}>{error}</Alert>}
      <Table striped bordered hover responsive>
        <thead>
          <tr><th>#</th><th>Name</th><th>Email</th><th>Phone</th><th>City</th><th>Gender</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {customers.map(c => (
            <tr key={c.id}>
              <td>{c.id}</td><td>{c.name}</td><td>{c.email}</td><td>{c.phoneNumber}</td>
              <td>{c.city}</td><td>{c.gender}</td>
              <td>
                <Button variant="outline-primary" size="sm" className="me-1" onClick={() => openEdit(c)}>Edit</Button>
                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(c.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton><Modal.Title>{editing ? 'Edit Customer' : 'New Customer'}</Modal.Title></Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3"><Form.Label>Name</Form.Label><Form.Control value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Email</Form.Label><Form.Control type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Phone</Form.Label><Form.Control value={form.phoneNumber} onChange={e => setForm({ ...form, phoneNumber: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Address</Form.Label><Form.Control value={form.address} onChange={e => setForm({ ...form, address: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>City</Form.Label><Form.Control value={form.city} onChange={e => setForm({ ...form, city: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Gender</Form.Label>
              <Form.Select value={form.gender} onChange={e => setForm({ ...form, gender: e.target.value })}>
                <option>Male</option><option>Female</option><option>Other</option>
              </Form.Select>
            </Form.Group>
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

export default CustomersPage;
