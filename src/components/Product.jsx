import React from 'react';
import './Product.css';

const Product = ({ product }) => {
  return (
    <li className="product-item">
      <h3 className="product-title">{product.name}</h3>
      <p>{product.description}</p>
      <p className="product-price">Price: ${product.price}</p>
      <button className="view-details-button">View Details</button>
    </li>
  );
};

export default Product;
