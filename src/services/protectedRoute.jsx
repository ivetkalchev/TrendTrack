import React from 'react';
import { Navigate } from 'react-router-dom';
import TokenManager from '../services/tokenManager';

const ProtectedRoute = ({ element }) => {
  const isAuthenticated = TokenManager.isAuthenticated();

  if (!isAuthenticated) {
    return <Navigate to="/authentication" />;
  }

  return element;
};

export default ProtectedRoute;