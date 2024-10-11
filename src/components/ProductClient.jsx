import React, { useState } from 'react';
import './ProductClient.css';

const ProductClient = ({ product }) => {
  const [quantity, setQuantity] = useState(1);

  const handleBuyClick = () => {
    alert(`You have selected ${quantity} of ${product.name} for purchase!`);
  };

  const handleQuantityChange = (e) => {
    const value = Number(e.target.value);
    if (value <= product.quantity && value >= 1) {
      setQuantity(value);
    }
  };

  return (
    <li className="product-item">
      <h3 className="product-title">{product.name}</h3>
      <p className="product-description">{product.description}</p>
      <p className="product-description">Color: {product.color}</p>
      <p className="product-price">Price: €{product.price}</p>

      <div className="quantity-container">
        <input
          type="number"
          id={`quantity-${product.idProduct}`}
          value={quantity}
          min="1"
          max={product.quantity}
          onChange={handleQuantityChange}
          className="quantity-input"
        />
      </div>

      <button className="buy-button" onClick={handleBuyClick}>Purchase</button>
    </li>
  );
};

export default ProductClient;
