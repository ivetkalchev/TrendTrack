import React from 'react';

const Product = ({ product, onEdit, onDelete }) => {
  return (
    <li className="product-item">
      <h3 className="product-title">{product.name}</h3>
      <p className="product-price">Price: ${product.price}</p>
      <p className="product-quantity">Quantity: {product.quantity}</p>
      <p className="product-description">{product.description}</p>
      <button className="edit-button" onClick={onEdit}>Edit</button>
      <button className="delete-button" onClick={onDelete}>Delete</button>
    </li>
  );
};

export default Product;