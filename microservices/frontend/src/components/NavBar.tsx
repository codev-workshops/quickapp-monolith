import React from 'react';
import { Navbar, Nav, Container, NavDropdown } from 'react-bootstrap';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const NavBar: React.FC = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) return null;

  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
      <Container fluid>
        <Navbar.Brand as={Link} to="/">QuickApp</Navbar.Brand>
        <Navbar.Toggle aria-controls="main-nav" />
        <Navbar.Collapse id="main-nav">
          <Nav className="me-auto" activeKey={location.pathname}>
            <Nav.Link as={Link} to="/" active={location.pathname === '/'}>Home</Nav.Link>
            <Nav.Link as={Link} to="/customers" active={location.pathname === '/customers'}>Customers</Nav.Link>
            <Nav.Link as={Link} to="/products" active={location.pathname === '/products'}>Products</Nav.Link>
            <Nav.Link as={Link} to="/orders" active={location.pathname === '/orders'}>Orders</Nav.Link>
            <Nav.Link as={Link} to="/about" active={location.pathname === '/about'}>About</Nav.Link>
          </Nav>
          <Nav>
            <NavDropdown title={user?.fullName || user?.userName || 'User'} id="user-dropdown" align="end">
              <NavDropdown.Item as={Link} to="/settings">Settings</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item onClick={logout}>Logout</NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavBar;
