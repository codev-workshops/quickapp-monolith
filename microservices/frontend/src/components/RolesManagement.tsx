import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Alert, Spinner } from 'react-bootstrap';
import { roleService } from '../services/role.service';
import type { Role, Permission } from '../models';

const RolesManagement: React.FC = () => {
  const [roles, setRoles] = useState<Role[]>([]);
  const [permissions, setPermissions] = useState<Permission[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<Role | null>(null);
  const [form, setForm] = useState({ name: '', description: '', permissions: [] as string[] });

  const loadData = async () => {
    try {
      setLoading(true);
      const [r, p] = await Promise.all([roleService.getAll(), roleService.getPermissions()]);
      setRoles(r);
      setPermissions(p);
    } catch {
      setError('Failed to load roles');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadData(); }, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', description: '', permissions: [] });
    setShowModal(true);
  };

  const openEdit = (r: Role) => {
    setEditing(r);
    setForm({ name: r.name, description: r.description || '', permissions: r.permissions?.map(p => typeof p === 'string' ? p : p.value) || [] });
    setShowModal(true);
  };

  const handleSave = async () => {
    try {
      const payload = {
        name: form.name,
        description: form.description,
        permissions: permissions.filter(p => form.permissions.includes(p.value)),
      };
      if (editing) {
        await roleService.update(editing.id, payload);
      } else {
        await roleService.create(payload);
      }
      setShowModal(false);
      loadData();
    } catch {
      setError('Failed to save role');
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Delete this role?')) return;
    try {
      await roleService.delete(id);
      loadData();
    } catch {
      setError('Failed to delete role');
    }
  };

  const togglePermission = (value: string) => {
    setForm(prev => ({
      ...prev,
      permissions: prev.permissions.includes(value)
        ? prev.permissions.filter(p => p !== value)
        : [...prev.permissions, value]
    }));
  };

  if (loading) return <div className="text-center mt-3"><Spinner animation="border" /></div>;

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5>Roles</h5>
        <Button variant="primary" size="sm" onClick={openCreate}>New Role</Button>
      </div>
      {error && <Alert variant="danger" dismissible onClose={() => setError('')}>{error}</Alert>}
      <Table striped bordered hover responsive size="sm">
        <thead><tr><th>Name</th><th>Description</th><th>Users</th><th>Permissions</th><th>Actions</th></tr></thead>
        <tbody>
          {roles.map(r => (
            <tr key={r.id}>
              <td>{r.name}</td><td>{r.description}</td><td>{r.usersCount}</td>
              <td>{r.permissions?.map(p => typeof p === 'string' ? p : p.value).join(', ')}</td>
              <td>
                <Button variant="outline-primary" size="sm" className="me-1" onClick={() => openEdit(r)}>Edit</Button>
                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(r.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton><Modal.Title>{editing ? 'Edit Role' : 'New Role'}</Modal.Title></Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3"><Form.Label>Name</Form.Label><Form.Control value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Description</Form.Label><Form.Control as="textarea" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Permissions</Form.Label>
              {permissions.map(p => (
                <Form.Check key={p.value} type="checkbox" label={`${p.name} (${p.value})`}
                  checked={form.permissions.includes(p.value)} onChange={() => togglePermission(p.value)} />
              ))}
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
          <Button variant="primary" onClick={handleSave}>Save</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default RolesManagement;
