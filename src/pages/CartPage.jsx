import React, { useEffect, useState } from "react";
import cartService from "../services/cartService";
import CartItem from "../components/CartItem";
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
        console.error("Error fetching cart:", err.message); // Log the error
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
      console.error("Error removing item:", err.message); // Log the error
      setError(err.message);
    }
  };

  const handleUpdateQuantity = async (fabricId, quantity) => {
    try {
      const updatedCart = await cartService.updateCartItem(fabricId, quantity);
      setCart(updatedCart);
    } catch (err) {
      console.error("Error updating item quantity:", err.message); // Log the error
      setError(err.message);
    }
  };

  if (!cart) {
    return <div>Loading...</div>;
  }

  return (
    <div className="cart-page">
      <h1>Your Cart</h1>
      {error && <p className="inline-error">There was an error: {error}</p>}
      <div className="cart-items">
        {cart.items.map((item) => (
          <CartItem
            key={item.fabric.id}
            item={item}
            onRemove={handleRemoveItem}
            onUpdateQuantity={handleUpdateQuantity}
          />
        ))}
      </div>
      <h2>Total Cost: €{cart.totalCost.toFixed(2)}</h2>
    </div>
  );
};

export default CartPage;