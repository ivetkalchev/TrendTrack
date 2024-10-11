import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ProductAdmin from '../components/ProductAdmin';
import Filter from '../components/Filter';
import './ProductManagementPage.css';

const ProductManagementPage = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filteredProducts, setFilteredProducts] = useState([]);

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
    const filtered = products.filter(product => {
      const matchesName = name ? product.name.toLowerCase().includes(name.toLowerCase()) : true;
      const matchesColor = color ? product.color === color : true;
      return matchesName && matchesColor;
    });
    setFilteredProducts(filtered);
  };

  const handleDelete = (id) => {
    axios.delete(`http://localhost:8080/products/${id}`)
      .then(() => {
        setProducts(products.filter(product => product.id !== id));
        setFilteredProducts(filteredProducts.filter(product => product.id !== id));
        fetchProducts();
      })
      .catch((error) => {
        setError(error.message);
      });
  };  

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="product-management-container">
      <h2>Product Management</h2>
      <Filter onFilter={handleFilter} />
      {filteredProducts.length === 0 ? (
        <p>No products available.</p>
      ) : (
        <div className="product-list">
          {filteredProducts.map((product) => (
            <ProductAdmin
              key={`${product.id}`}
              product={product}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductManagementPage;