import React from 'react';
import Header from './components/Header';
import { Routes, Route } from 'react-router-dom';
import ProductList from './components/ProductList';
import Welcome from './components/Welcome';

function App() {
  return (
    <>
      <Header />
      <div className="container">
        <Routes>
        <Route path="/" element={<Welcome />} />
          <Route path="/products" element={<ProductList />} />
          {}
        </Routes>
      </div>
    </>
  );
}

export default App;
