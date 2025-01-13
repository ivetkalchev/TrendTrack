import React, { useState } from "react";
import "./PurchaseFabric.css";
import { FaImage, FaCheckCircle, FaTimesCircle } from "react-icons/fa";
import cartService from "../services/cartService";

const PurchaseFabric = ({ product }) => {
  const [addedToCart, setAddedToCart] = useState(false);

  const handleAddToCart = async () => {
    if (product.stock === 0) return;
  
    try {
      await cartService.addToCart(product.id, 1);
  
      setAddedToCart(true);
      setTimeout(() => setAddedToCart(false), 2000);
    } catch (err) {
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };  

  return (
    <div className="product-admin-item">
      <div className="product-image">
        {product.pictureUrl ? (
          <img src={product.pictureUrl} alt={product.name} />
        ) : (
          <div className="placeholder-image">
            <FaImage size={50} color="#ddd" />
          </div>
        )}
      </div>
      <div className="product-header">
        <h3 className="product-title">{product.name}</h3>
        <span>€{product.price.toFixed(2)}</span>
      </div>
      <p>[{product.material}] [{product.color}]</p>
      <p>{product.description}</p>
      <div className="product-icons-row">
        <div className="icon-item">
          {product.washable ? (
            <FaCheckCircle size={20} color="green" title="Washable" />
          ) : (
            <FaTimesCircle size={20} color="red" title="Not Washable" />
          )}
          <span>Washable</span>
        </div>
        <div className="icon-item">
          {product.ironed ? (
            <FaCheckCircle size={20} color="green" title="Ironed" />
          ) : (
            <FaTimesCircle size={20} color="red" title="Not Ironed" />
          )}
          <span>Ironed</span>
        </div>
        <div className="button-container">
          <button
            className={`cart-button ${product.stock === 0 ? 'disabled' : ''}`}
            onClick={handleAddToCart}
            disabled={product.stock === 0}
          >
            {product.stock === 0 ? 'Sold Out' : 'Add To Cart'}
          </button>
          {addedToCart && <span className="added-message">Added to cart!</span>}
        </div>
      </div>
    </div>
  );
};

export default PurchaseFabric;