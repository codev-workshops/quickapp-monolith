import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Container className="mt-5 text-center">
      <h1 className="display-1 text-muted">404</h1>
      <h3>Page Not Found</h3>
      <p className="text-muted">The page you are looking for does not exist.</p>
      <Button variant="primary" onClick={() => navigate('/')}>Go Home</Button>
    </Container>
  );
};

export default NotFoundPage;
