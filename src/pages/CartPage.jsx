import React, { useEffect, useState } from "react";
import cartService from "../services/cartService";
import orderService from "../services/orderService";
import CartItem from "../components/CartItem";
import "./CartPage.css";

const CartPage = () => {
  const [cart, setCart] = useState(null);
  const [address, setAddress] = useState("");
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

  const recalculateTotalCost = (items) => {
    return items.reduce((total, item) => total + item.totalPrice, 0);
  };

  const handleRemoveItem = async (fabricId) => {
    try {
      await cartService.removeFromCart(fabricId);
      alert("Item removed successfully!");
      setCart((prevCart) => {
        const updatedItems = prevCart.items.filter((item) => item.fabric.id !== fabricId);
        return {
          ...prevCart,
          items: updatedItems,
          totalCost: recalculateTotalCost(updatedItems),
        };
      });
    } catch (err) {
      console.error("Error removing item:", err.response?.data || err.message);
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };

  const handleUpdateQuantity = async (fabricId, quantity) => {
    try {
      const updatedCart = await cartService.updateCartItem(fabricId, quantity);
      setCart(updatedCart);
    } catch (err) {
      console.error("Error updating item quantity:", err.message);
      setError(err.message);
    }
  };

  const handleProceedOrder = async () => {
    if (!address.trim()) {
      alert("Please enter a valid address.");
      return;
    }

    try {
      const orderData = {
        user: cart.user,
        items: cart.items.map((item) => ({
          fabric: item.fabric,
          quantity: item.quantity,
          pricePerUnit: item.fabric.price,
          totalPrice: item.totalPrice,
        })),
        address,
        totalAmount: cart.totalCost,
      };
      
      await orderService.createOrder(orderData);
      alert("Order placed successfully!");

      setCart({ ...cart, items: [], totalCost: 0 });
      setAddress("");
    } catch (err) {
      console.error("Error proceeding with order:", err.message);
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };

  if (!cart) {
    return <div>Loading...</div>;
  }

  return (
    <div className="cart-page">
      <h1>Your Cart</h1>
      {error && <p className="inline-error">There was an error: {error}</p>}
      {cart.items.length === 0 ? (
        <div className="empty-cart-message">
          <p>Your cart is empty!</p>
        </div>
      ) : (
        <>
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
          <div className="order-section">
            <label htmlFor="address">Delivery Address:</label>
            <input
              id="address"
              type="text"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="Enter your delivery address"
              className="address-input"
            />
            <button className="proceed-order-button" onClick={handleProceedOrder}>
              Proceed with Order
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default CartPage;