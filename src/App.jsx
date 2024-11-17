import React from 'react';

import Header from './components/Header';
import HomePage from './pages/HomePage';
import Footer from './components/Footer';
import FabricManagementPage from './pages/FabricManagementPage';
import AuthenticationPage from './pages/AuthenticationPage';

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

const App = () => {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={ <HomePage />} />
        <Route path="/fabric-management" element={ <FabricManagementPage />} />
        <Route path="/authentication" element={ <AuthenticationPage />} />
      </Routes>
      <Footer />
    </Router>
  );
};

export default App;