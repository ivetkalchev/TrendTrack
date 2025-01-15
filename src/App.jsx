import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import Footer from './components/Footer';
import FabricManagementPage from './pages/FabricManagementPage';
import AuthenticationPage from './pages/AuthenticationPage';
import UserManagementPage from './pages/UserManagementPage';
import PersonalInfoPage from './pages/PersonalInfoPage';
import CataloguePage from './pages/CataloguePage';
import CartPage from './pages/CartPage';
import OrderPage from './pages/OrderPage';
import UserOrdersPage from './pages/UserOrdersPage';
import StatisticsPage from './pages/StatisticsPage';
import { Client } from '@stomp/stompjs';
import TokenManager from './services/tokenManager';
import Notification from './components/Notification';
import ProtectedRoute from './services/protectedRoute';

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

const App = () => {
  const [notification, setNotification] = useState('');

  const setupStompClient = (username) => {
    // stomp client over websockets
    const stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    stompClient.onConnect = () => {
      // subscribe to the backend public topic
      stompClient.subscribe('/topic/publicmessages', (data) => {
        console.log(data);
        onMessageReceived(data);
      });
      // subscribe to the backend "private" topic
      stompClient.subscribe(`/user/${username}/queue/notification`, (data) => {
        onMessageReceived(data);
      });
    };

    // initiate client
    stompClient.activate();
  };

  // display the received data
  const onMessageReceived = (data) => {
    const message = JSON.parse(data.body);
    setNotification(message.text);
  };

  useEffect(() => {
    setupStompClient(TokenManager.getUserIdFromToken());
  }, []);

  return (
    <Router>
      {notification && <Notification message={notification} />}
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/fabric-management" element={<ProtectedRoute element={<FabricManagementPage />} />} />
        <Route path="/authentication" element={<AuthenticationPage />} />
        <Route path="/user-management" element={<ProtectedRoute element={<UserManagementPage />} />} />
        <Route path="/my-account" element={<ProtectedRoute element={<PersonalInfoPage />} />} />
        <Route path="/catalogue" element={<CataloguePage />} />
        <Route path="/cart" element={<ProtectedRoute element={<CartPage />} />} />
        <Route path="/orders" element={<ProtectedRoute element={<OrderPage />} />} />
        <Route path="/my-orders" element={<ProtectedRoute element={<UserOrdersPage />} />} />
        <Route path="/statistics" element={<ProtectedRoute element={<StatisticsPage />} />} />
      </Routes>
      <Footer />
    </Router>
  );
};

export default App;