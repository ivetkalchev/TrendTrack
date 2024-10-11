import React from 'react';
import './ProductAdmin.css';

const ProductAdmin = ({ product, onDelete }) => {
  const handleDelete = () => {
    onDelete(product.id);
  };

  return (
    <div className="product-admin-item">
      <h3 className="product-title">{product.name}</h3>
      <p className="product-description">Color: {product.color}</p>
      <p className="product-price">Price: €{product.price}</p>
      <p className="product-description">Quantity: {product.quantity}</p>
      <div className="button-container">
        <button onClick={handleDelete} className="delete-button">Delete</button>
      </div>
    </div>
  );
};

export default ProductAdmin;