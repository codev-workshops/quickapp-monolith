import React from 'react';
import { Container, Card, Row, Col } from 'react-bootstrap';

const AboutPage: React.FC = () => {
  return (
    <Container className="mt-4">
      <h2 className="page-header">About QuickApp</h2>
      <Row>
        <Col md={8}>
          <Card className="mb-4">
            <Card.Body>
              <h5>QuickApp — Microservices Edition</h5>
              <p>
                This application is a decomposition of the QuickApp .NET/Angular monolith into
                a microservices architecture using Spring Boot and React.
              </p>
              <h6>Architecture</h6>
              <ul>
                <li><strong>API Gateway</strong> — Spring Cloud Gateway routing to all services</li>
                <li><strong>Auth Service</strong> — JWT authentication, user and role management</li>
                <li><strong>Customer Service</strong> — Customer CRUD operations</li>
                <li><strong>Product Service</strong> — Product and category management</li>
                <li><strong>Order Service</strong> — Order management with cross-service enrichment</li>
                <li><strong>Frontend</strong> — React + Vite + TypeScript + React-Bootstrap</li>
              </ul>
              <h6>Tech Stack</h6>
              <ul>
                <li>Backend: Spring Boot 3.4.4, Java 17, Spring Security, Spring Data JPA</li>
                <li>Database: SQLite (per-service)</li>
                <li>Frontend: React 18, TypeScript, Vite, React-Bootstrap</li>
                <li>Auth: JWT (HS512)</li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card>
            <Card.Header>Credits</Card.Header>
            <Card.Body>
              <p>Original monolith by <strong>Ebenezer Monney</strong></p>
              <p>Microservices decomposition as a workshop exercise</p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default AboutPage;
