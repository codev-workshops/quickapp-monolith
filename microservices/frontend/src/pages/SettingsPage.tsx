import React, { useState } from 'react';
import { Container, Tab, Tabs } from 'react-bootstrap';
import UsersManagement from '../components/UsersManagement';
import RolesManagement from '../components/RolesManagement';
import ProfileSettings from '../components/ProfileSettings';

const SettingsPage: React.FC = () => {
  const [key, setKey] = useState('profile');

  return (
    <Container className="mt-4">
      <h2 className="page-header">Settings</h2>
      <Tabs activeKey={key} onSelect={(k) => setKey(k || 'profile')} className="mb-3">
        <Tab eventKey="profile" title="Profile">
          <ProfileSettings />
        </Tab>
        <Tab eventKey="users" title="Users Management">
          <UsersManagement />
        </Tab>
        <Tab eventKey="roles" title="Roles Management">
          <RolesManagement />
        </Tab>
      </Tabs>
    </Container>
  );
};

export default SettingsPage;
