import React, { useState } from 'react';
import './ProductAdmin.css';
import EditProductForm from './EditProduct';

const ProductAdmin = ({ product, onDelete, onUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);

  const handleEdit = (updatedProduct) => {
    onUpdate(updatedProduct);
    setIsEditing(false);
  };

  return (
    <div className="product-admin-item">
      <h3 className="product-title">{product.name}</h3>
      <p className="product-description">{product.description}</p>
      <p className="product-description">Material: {product.material}</p>
      <p className="product-description">Color: {product.color}</p>
      <p className="product-price">Price: €{product.price}</p>
      <p className="product-description">Washable: {product.washable ? 'Yes' : 'No'}</p>
      <p className="product-description">Ironed: {product.ironed ? 'Yes' : 'No'}</p>
      <p className="product-description">Stock: {product.stock}</p>
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