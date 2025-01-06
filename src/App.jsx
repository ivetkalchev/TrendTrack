import React from 'react';
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

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

const App = () => {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/fabric-management" element={<FabricManagementPage />} />
        <Route path="/authentication" element={<AuthenticationPage />} />
        <Route path="/user-management" element={<UserManagementPage />} />
        <Route path="/my-account" element={<PersonalInfoPage />} />
        <Route path="/catalogue" element={<CataloguePage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/orders" element={<OrderPage />} />
        <Route path="/my-orders" element={<UserOrdersPage />} />
        <Route path="/statistics" element={<StatisticsPage />} />
      </Routes>
      <Footer />
    </Router>
  );
};

export default App;