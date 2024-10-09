import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Product from '../components/Product';
import '../components/Product.css'; 

const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newProduct, setNewProduct] = useState({ name: '', price: '', quantity: '', description: '' });

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = () => {
    axios.get('http://localhost:8080/products')
      .then((response) => {
        setProducts(response.data.products);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };  

  const handleAddProduct = (e) => {
    e.preventDefault();
    axios.post('http://localhost:8080/products', newProduct)
      .then(() => {
        setNewProduct({ name: '', price: '', quantity: '', description: '' });
        fetchProducts();
      })
      .catch((error) => {
        setError(error.message);
      });
  };  

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <h2>Product List</h2>
      <form onSubmit={handleAddProduct}>
        <input 
          type="text" 
          placeholder="Name" 
          value={newProduct.name} 
          onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })} 
          required 
        />
        <input 
          type="number" 
          placeholder="Price" 
          value={newProduct.price} 
          onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })} 
          required 
        />
        <input 
          type="number" 
          placeholder="Quantity" 
          value={newProduct.quantity} 
          onChange={(e) => setNewProduct({ ...newProduct, quantity: e.target.value })} 
          required 
        />
        <input 
          type="text" 
          placeholder="Description" 
          value={newProduct.description} 
          onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })} 
          required 
        />
        <button type="submit">Add Product</button>
      </form>

      {products.length === 0 ? (
        <p>No products available.</p>
      ) : (
        <ul className="product-list">
          {products.map((product) => (
            <Product product={product} key={product.idProduct} />
          ))}
        </ul>
      )}
    </div>
  );  
};

export default ProductsPage;