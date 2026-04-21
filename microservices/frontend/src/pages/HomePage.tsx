import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';
import TodoDemo from '../components/TodoDemo';

const HomePage: React.FC = () => {
  const { user } = useAuth();

  return (
    <Container className="mt-4">
      <h2 className="page-header">Dashboard</h2>
      <Row>
        <Col md={8}>
          <Card className="mb-4">
            <Card.Header>Welcome</Card.Header>
            <Card.Body>
              <h5>Hello, {user?.fullName || user?.userName}!</h5>
              <p>Welcome to QuickApp — a microservices-based application built with Spring Boot and React.</p>
              <hr />
              <Row>
                <Col sm={3} className="text-center">
                  <h3 className="text-primary">4+</h3>
                  <p className="text-muted">Microservices</p>
                </Col>
                <Col sm={3} className="text-center">
                  <h3 className="text-success">14+</h3>
                  <p className="text-muted">Customers</p>
                </Col>
                <Col sm={3} className="text-center">
                  <h3 className="text-info">12+</h3>
                  <p className="text-muted">Products</p>
                </Col>
                <Col sm={3} className="text-center">
                  <h3 className="text-warning">12+</h3>
                  <p className="text-muted">Orders</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
          <Card className="mb-4">
            <Card.Header>System Info</Card.Header>
            <Card.Body>
              <p><strong>Architecture:</strong> Microservices</p>
              <p><strong>API Gateway:</strong> Spring Cloud Gateway (port 8080)</p>
              <p><strong>Auth Service:</strong> Spring Boot + JWT (port 8081)</p>
              <p><strong>Customer Service:</strong> Spring Boot + JPA (port 8082)</p>
              <p><strong>Product Service:</strong> Spring Boot + JPA (port 8083)</p>
              <p><strong>Order Service:</strong> Spring Boot + JPA (port 8084)</p>
              <p><strong>Frontend:</strong> React + Vite + TypeScript</p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <TodoDemo />
        </Col>
      </Row>
    </Container>
  );
};

export default HomePage;
