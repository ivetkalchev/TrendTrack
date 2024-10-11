import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ProductClient from '../components/ProductClient';
import Filter from '../components/Filter';
import './ProductsPage.css';

const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = () => {
    axios.get('http://localhost:8080/products')
      .then((response) => {
        setProducts(response.data.products);
        setFilteredProducts(response.data.products);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleFilter = (name, color) => {
    let filtered = products;

    if (name) {
      filtered = filtered.filter((product) =>
        product.name.toLowerCase().includes(name.toLowerCase())
      );
    }

    if (color) {
      filtered = filtered.filter((product) => product.color === color);
    }

    setFilteredProducts(filtered);
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="products-container">
      <h2>Product List</h2>
      
      <Filter onFilter={handleFilter} /> {}

      {filteredProducts.length === 0 ? (
        <p>No products available.</p>
      ) : (
        <ul className="product-list">
          {filteredProducts.map((product) => (
            <ProductClient product={product} key={product.idProduct} />
          ))}
        </ul>
      )}
    </div>
  );  
};

export default ProductsPage;