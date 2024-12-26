import React, { useEffect, useState } from "react";
import cartService from "../services/cartService";
import "./CartPage.css";

const CartPage = () => {
  const [cart, setCart] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const data = await cartService.getCart();
        setCart(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchCart();
  }, []);

  const handleRemoveItem = async (fabricId) => {
    try {
      const updatedCart = await cartService.removeFromCart(fabricId);
      setCart(updatedCart);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleUpdateQuantity = async (fabricId, quantity) => {
    try {
      const updatedCart = await cartService.updateCartItem(fabricId, quantity);
      setCart(updatedCart);
    } catch (err) {
      setError(err.message);
    }
  };

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!cart) {
    return <div>Loading...</div>;
  }

  return (
    <div className="cart-page">
      <h1>Your Cart</h1>
      <div className="cart-items">
        {cart.items.map((item) => (
          <div key={item.fabric.id} className="cart-item">
            <img src={item.fabric.pictureUrl || "placeholder.jpg"} alt={item.fabric.name} />
            <div className="cart-item-details">
              <h3>{item.fabric.name}</h3>
              <p>Price: €{item.fabric.price.toFixed(2)}</p>
              <p>Total: €{item.totalPrice.toFixed(2)}</p>
              <div>
                <label htmlFor={`quantity-${item.fabric.id}`}>Quantity:</label>
                <input
                  id={`quantity-${item.fabric.id}`}
                  type="number"
                  min="1"
                  value={item.quantity}
                  onChange={(e) => handleUpdateQuantity(item.fabric.id, parseInt(e.target.value))}
                />
              </div>
              <button onClick={() => handleRemoveItem(item.fabric.id)}>Remove</button>
            </div>
          </div>
        ))}
      </div>
      <h2>Total Cost: €{cart.totalCost.toFixed(2)}</h2>
    </div>
  );
};

export default CartPage;