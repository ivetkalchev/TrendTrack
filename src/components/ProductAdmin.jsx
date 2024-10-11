import React, { useState } from 'react';
import './ProductAdmin.css';
import EditProductForm from './EditProduct';

const ProductAdmin = ({ product, onDelete, onUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);

  const handleEdit = (updatedProduct) => {
    onUpdate(updatedProduct);
    setIsEditing(false); // Close the editing form after updating
  };

  return (
    <div className="product-admin-item">
      <h3 className="product-title">{product.name}</h3>
      <p className="product-description">Color: {product.color}</p>
      <p className="product-price">Price: €{product.price}</p>
      <p className="product-description">Quantity: {product.quantity}</p>
      <p className="product-description">{product.description}</p>
      <div className="button-container">
        <button onClick={() => setIsEditing(true)} className="edit-button">Edit</button>
        <button onClick={() => onDelete(product.id)} className="delete-button">Delete</button>
      </div>
      {isEditing && (
        <EditProductForm
          product={product}
          onUpdate={handleEdit}
          onClose={() => setIsEditing(false)}
        />
      )}
    </div>
  );
};

export default ProductAdmin;