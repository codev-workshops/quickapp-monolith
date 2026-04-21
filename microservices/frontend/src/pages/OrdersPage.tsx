import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Modal, Form, Alert, Spinner, Card } from 'react-bootstrap';
import { orderService } from '../services/order.service';
import type { Order } from '../models';

const OrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [showDetail, setShowDetail] = useState(false);

  const loadOrders = async () => {
    try {
      setLoading(true);
      const data = await orderService.getAll();
      setOrders(data);
    } catch {
      setError('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadOrders(); }, []);

  const viewDetails = async (id: number) => {
    try {
      const order = await orderService.getById(id);
      setSelectedOrder(order);
      setShowDetail(true);
    } catch {
      setError('Failed to load order details');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Delete this order?')) return;
    try {
      await orderService.delete(id);
      loadOrders();
    } catch {
      setError('Failed to delete order');
    }
  };

  if (loading) return <Container className="mt-4 text-center"><Spinner animation="border" /></Container>;

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center page-header">
        <h2>Orders</h2>
      </div>
      {error && <Alert variant="danger" dismissible onClose={() => setError('')}>{error}</Alert>}
      <Table striped bordered hover responsive>
        <thead>
          <tr><th>#</th><th>Customer</th><th>Cashier</th><th>Discount</th><th>Comments</th><th>Items</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {orders.map(o => (
            <tr key={o.id}>
              <td>{o.id}</td><td>{o.customerName || `Customer #${o.customerId}`}</td>
              <td>{o.cashierName || o.cashierId}</td><td>${o.discount}</td>
              <td>{o.comments}</td><td>{o.orderDetails?.length || 0}</td>
              <td>
                <Button variant="outline-info" size="sm" className="me-1" onClick={() => viewDetails(o.id)}>Details</Button>
                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(o.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showDetail} onHide={() => setShowDetail(false)} size="lg">
        <Modal.Header closeButton><Modal.Title>Order #{selectedOrder?.id} Details</Modal.Title></Modal.Header>
        <Modal.Body>
          {selectedOrder && (
            <>
              <Card className="mb-3">
                <Card.Body>
                  <p><strong>Customer:</strong> {selectedOrder.customerName || `#${selectedOrder.customerId}`}</p>
                  <p><strong>Cashier:</strong> {selectedOrder.cashierName || selectedOrder.cashierId}</p>
                  <p><strong>Discount:</strong> ${selectedOrder.discount}</p>
                  <p><strong>Comments:</strong> {selectedOrder.comments || 'N/A'}</p>
                </Card.Body>
              </Card>
              <h6>Line Items</h6>
              <Table striped bordered size="sm">
                <thead><tr><th>Product</th><th>Unit Price</th><th>Qty</th><th>Discount</th><th>Total</th></tr></thead>
                <tbody>
                  {selectedOrder.orderDetails?.map(d => (
                    <tr key={d.id}>
                      <td>{d.productName || `Product #${d.productId}`}</td>
                      <td>${d.unitPrice}</td><td>{d.quantity}</td><td>${d.discount}</td>
                      <td>${(d.unitPrice * d.quantity - d.discount).toLocaleString()}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </>
          )}
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default OrdersPage;
