import React, { useState, useEffect } from 'react';
import { Card, Form, Button, ListGroup, InputGroup } from 'react-bootstrap';
import type { TodoItem } from '../models';

const STORAGE_KEY = 'quickapp_todos';

const TodoDemo: React.FC = () => {
  const [todos, setTodos] = useState<TodoItem[]>(() => {
    const stored = localStorage.getItem(STORAGE_KEY);
    return stored ? JSON.parse(stored) : [
      { id: 1, name: 'Create a microservices architecture', completed: true },
      { id: 2, name: 'Build React frontend', completed: true },
      { id: 3, name: 'Implement JWT authentication', completed: false },
      { id: 4, name: 'Write comparison tests', completed: false },
    ];
  });
  const [newTodo, setNewTodo] = useState('');

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
  }, [todos]);

  const addTodo = () => {
    if (!newTodo.trim()) return;
    setTodos([...todos, { id: Date.now(), name: newTodo.trim(), completed: false }]);
    setNewTodo('');
  };

  const toggleTodo = (id: number) => {
    setTodos(todos.map(t => t.id === id ? { ...t, completed: !t.completed } : t));
  };

  const removeTodo = (id: number) => {
    setTodos(todos.filter(t => t.id !== id));
  };

  return (
    <Card>
      <Card.Header>Todo Demo</Card.Header>
      <Card.Body>
        <InputGroup className="mb-3">
          <Form.Control
            placeholder="Add a new task..."
            value={newTodo}
            onChange={(e) => setNewTodo(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && addTodo()}
          />
          <Button variant="primary" onClick={addTodo}>Add</Button>
        </InputGroup>
        <ListGroup>
          {todos.map((todo) => (
            <ListGroup.Item key={todo.id} className="d-flex align-items-center">
              <Form.Check
                type="checkbox"
                checked={todo.completed}
                onChange={() => toggleTodo(todo.id)}
                className="me-2"
              />
              <span style={{ textDecoration: todo.completed ? 'line-through' : 'none', flex: 1 }}>
                {todo.name}
              </span>
              <Button variant="outline-danger" size="sm" onClick={() => removeTodo(todo.id)}>
                &times;
              </Button>
            </ListGroup.Item>
          ))}
        </ListGroup>
        <div className="mt-2 text-muted small">
          {todos.filter(t => !t.completed).length} remaining / {todos.length} total
        </div>
      </Card.Body>
    </Card>
  );
};

export default TodoDemo;
