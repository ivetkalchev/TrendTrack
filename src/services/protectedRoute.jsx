import React from 'react';
import { Navigate } from 'react-router-dom';
import TokenManager from '../services/tokenManager';

const ProtectedRoute = ({ element, requiredRole }) => {
  const isAuthenticated = TokenManager.isAuthenticated();
  const role = TokenManager.getRole();

  const allowedRoles = requiredRole.split(',').map(role => role.trim());

  if (!isAuthenticated) {
    return <Navigate to="/authentication" />;
  }

  if (!allowedRoles.includes(role)) {
    return <Navigate to="/" />;
  }

  return element;
};

export default ProtectedRoute;