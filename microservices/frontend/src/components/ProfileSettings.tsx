import React, { useState } from 'react';
import { Card, Form, Button, Alert } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';
import { authService } from '../services/auth.service';

const ProfileSettings: React.FC = () => {
  const { user, refreshUser } = useAuth();
  const [form, setForm] = useState({
    fullName: user?.fullName || '',
    email: user?.email || '',
    phoneNumber: user?.phoneNumber || '',
    jobTitle: user?.jobTitle || '',
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSave = async () => {
    try {
      if (user) {
        await authService.updateUser(user.id, form);
        await refreshUser();
        setMessage('Profile updated successfully');
        setError('');
      }
    } catch {
      setError('Failed to update profile');
      setMessage('');
    }
  };

  return (
    <Card>
      <Card.Header>Profile</Card.Header>
      <Card.Body>
        {message && <Alert variant="success">{message}</Alert>}
        {error && <Alert variant="danger">{error}</Alert>}
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Username</Form.Label>
            <Form.Control value={user?.userName || ''} disabled />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Full Name</Form.Label>
            <Form.Control value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Email</Form.Label>
            <Form.Control type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Phone</Form.Label>
            <Form.Control value={form.phoneNumber} onChange={e => setForm({ ...form, phoneNumber: e.target.value })} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Job Title</Form.Label>
            <Form.Control value={form.jobTitle} onChange={e => setForm({ ...form, jobTitle: e.target.value })} />
          </Form.Group>
          <Button variant="primary" onClick={handleSave}>Save Changes</Button>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default ProfileSettings;
