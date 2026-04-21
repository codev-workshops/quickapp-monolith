import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Alert, Spinner } from 'react-bootstrap';
import { authService } from '../services/auth.service';
import { roleService } from '../services/role.service';
import type { User, Role } from '../models';

const UsersManagement: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<User | null>(null);
  const [form, setForm] = useState({ userName: '', fullName: '', email: '', phoneNumber: '', jobTitle: '', newPassword: '', roles: [] as string[], isEnabled: true });

  const loadData = async () => {
    try {
      setLoading(true);
      const [u, r] = await Promise.all([authService.getUsers(), roleService.getAll()]);
      setUsers(u);
      setRoles(r);
    } catch {
      setError('Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadData(); }, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ userName: '', fullName: '', email: '', phoneNumber: '', jobTitle: '', newPassword: '', roles: [], isEnabled: true });
    setShowModal(true);
  };

  const openEdit = (u: User) => {
    setEditing(u);
    setForm({ userName: u.userName, fullName: u.fullName || '', email: u.email || '', phoneNumber: u.phoneNumber || '', jobTitle: u.jobTitle || '', newPassword: '', roles: u.roles || [], isEnabled: u.isEnabled });
    setShowModal(true);
  };

  const handleSave = async () => {
    try {
      if (editing) {
        await authService.updateUser(editing.id, form);
      } else {
        await authService.createUser({ ...form, newPassword: form.newPassword || 'tempP@ss123' });
      }
      setShowModal(false);
      loadData();
    } catch {
      setError('Failed to save user');
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Delete this user?')) return;
    try {
      await authService.deleteUser(id);
      loadData();
    } catch {
      setError('Failed to delete user');
    }
  };

  const toggleRole = (roleName: string) => {
    setForm(prev => ({
      ...prev,
      roles: prev.roles.includes(roleName)
        ? prev.roles.filter(r => r !== roleName)
        : [...prev.roles, roleName]
    }));
  };

  if (loading) return <div className="text-center mt-3"><Spinner animation="border" /></div>;

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5>Users</h5>
        <Button variant="primary" size="sm" onClick={openCreate}>New User</Button>
      </div>
      {error && <Alert variant="danger" dismissible onClose={() => setError('')}>{error}</Alert>}
      <Table striped bordered hover responsive size="sm">
        <thead><tr><th>Username</th><th>Full Name</th><th>Email</th><th>Roles</th><th>Enabled</th><th>Actions</th></tr></thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.userName}</td><td>{u.fullName}</td><td>{u.email}</td>
              <td>{u.roles?.join(', ')}</td><td>{u.isEnabled ? 'Yes' : 'No'}</td>
              <td>
                <Button variant="outline-primary" size="sm" className="me-1" onClick={() => openEdit(u)}>Edit</Button>
                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(u.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton><Modal.Title>{editing ? 'Edit User' : 'New User'}</Modal.Title></Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3"><Form.Label>Username</Form.Label><Form.Control value={form.userName} onChange={e => setForm({ ...form, userName: e.target.value })} disabled={!!editing} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Full Name</Form.Label><Form.Control value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Email</Form.Label><Form.Control type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Phone</Form.Label><Form.Control value={form.phoneNumber} onChange={e => setForm({ ...form, phoneNumber: e.target.value })} /></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Job Title</Form.Label><Form.Control value={form.jobTitle} onChange={e => setForm({ ...form, jobTitle: e.target.value })} /></Form.Group>
            {!editing && <Form.Group className="mb-3"><Form.Label>Password</Form.Label><Form.Control type="password" value={form.newPassword} onChange={e => setForm({ ...form, newPassword: e.target.value })} /></Form.Group>}
            <Form.Group className="mb-3">
              <Form.Label>Roles</Form.Label>
              {roles.map(r => (
                <Form.Check key={r.id} type="checkbox" label={r.name} checked={form.roles.includes(r.name)}
                  onChange={() => toggleRole(r.name)} />
              ))}
            </Form.Group>
            <Form.Check className="mb-3" type="checkbox" label="Enabled" checked={form.isEnabled} onChange={e => setForm({ ...form, isEnabled: e.target.checked })} />
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

export default UsersManagement;
