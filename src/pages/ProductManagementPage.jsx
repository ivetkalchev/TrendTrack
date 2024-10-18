import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ProductAdmin from '../components/ProductAdmin';
import Filter from '../components/Filter';
import AddProduct from '../components/AddProduct';
import './ProductManagementPage.css';

const ProductManagementPage = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [isAdding, setIsAdding] = useState(false); 

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

  const handleAdd = (newProduct) => {
    axios.post('http://localhost:8080/products', newProduct) 
      .then((response) => {
        setProducts([...products, response.data]); 
        setFilteredProducts([...filteredProducts, response.data]); 
      })
      .catch((error) => {
        setError(error.message);
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
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  const handleUpdate = (updatedProduct) => {
    axios.put(`http://localhost:8080/products/${updatedProduct.id}`, updatedProduct)
      .then(() => {
        setProducts(products.map(product =>
          product.id === updatedProduct.id ? updatedProduct : product
        ));
        setFilteredProducts(filteredProducts.map(product =>
          product.id === updatedProduct.id ? updatedProduct : product
        ));
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
      <button onClick={() => setIsAdding(true)} className="add-product-button">Add Product</button>
      
      {isAdding && (
        <AddProduct
          onAdd={handleAdd}
          onClose={() => setIsAdding(false)}
        />
      )}

      {filteredProducts.length === 0 ? (
        <p>No results match your search.</p>
      ) : (
        <div className="product-list">
          {filteredProducts.map((product) => (
            <ProductAdmin
              key={`${product.id}`}
              product={product}
              onDelete={handleDelete}
              onUpdate={handleUpdate}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductManagementPage;