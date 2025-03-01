import React from "react";
import { FaTrashAlt } from "react-icons/fa";
import "./CartItem.css";

const CartItem = ({ item, onRemove, onUpdateQuantity }) => {
  const handleQuantityChange = (e) => {
    const newQuantity = parseInt(e.target.value, 10);
    if (newQuantity > 0) {
      onUpdateQuantity(item.fabric.id, newQuantity);
    }
  };

  return (
    <div className="cart-item">
      <div className="cart-item-image-container">
        <img
          src={item.fabric.pictureUrl || "placeholder.jpg"}
          alt={item.fabric.name}
          className="cart-item-image"
        />
      </div>
      <div className="cart-item-content">
        <h3 className="cart-item-name">{item.fabric.name}</h3>
        <p className="cart-item-price">
          Price: <strong>€{item.fabric.price.toFixed(2)}</strong>
        </p>
        <p className="cart-item-total">
          Total: <strong>€{item.totalPrice.toFixed(2)}</strong>
        </p>
        <div className="cart-item-quantity-container">
          <label htmlFor={`quantity-${item.fabric.id}`}>Quantity:</label>
          <input
            id={`quantity-${item.fabric.id}`}
            type="number"
            min="1"
            max={item.fabric.stock}
            value={item.quantity}
            onChange={handleQuantityChange}
            className="cart-item-quantity"
          />
        </div>
      </div>
      <button
        className="cart-item-remove-button"
        onClick={() => onRemove(item.fabric.id)}
      >
        <FaTrashAlt size={16} />
      </button>
    </div>
  );
};

export default CartItem;