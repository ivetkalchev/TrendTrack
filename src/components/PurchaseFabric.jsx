import React from "react";
import "./PurchaseFabric.css";
import { FaImage, FaCheckCircle, FaTimesCircle } from 'react-icons/fa';

const placeholderImage = (
  <div className="placeholder-image">
    <FaImage size={50} color="#ddd" />
  </div>
);

const PurchaseFabric = ({ product }) => {
  return (
    <div className="product-admin-item">
      <div className="product-image">
        {product.pictureUrl ? (
          <img src={product.pictureUrl} alt={product.name} />
        ) : (
          placeholderImage
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
            <div className="button-container">
              <button className="edit-button">Add To Cart</button>
            </div>
        </div>
      </div>
    </div>
  );
};

export default PurchaseFabric;