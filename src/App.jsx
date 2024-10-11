import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import ProductsPage from './pages/ProductsPage';
import AboutUsPage from './pages/AboutUsPage';
import HomePage from './pages/HomePage';
import Footer from './components/Footer';
import ProductManagementPage from './pages/ProductManagementPage';

const App = () => {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/aboutus" element={<AboutUsPage />} />
        <Route path="/products" element={<ProductsPage />} />
        <Route path="/productmanagement" element={<ProductManagementPage />} />
      </Routes>
      <Footer />
    </Router>
  );
};

export default App;