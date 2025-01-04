import React, { useEffect, useState } from "react";
import orderService from "../services/orderService";
import "./UserOrdersPage.css";

const UserOrdersPage = () => {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserOrders = async () => {
      try {
        const data = await orderService.getOrdersByUserId();
        setOrders(data.orders);
      } catch (err) {
        console.error("Error fetching user orders:", err.message);
        setError("Failed to load your orders. Please try again.");
      }
    };

    fetchUserOrders();
  }, []);

  return (
    <div className="user-orders-page">
      <h1>Your Orders</h1>
      {error && <p className="error-message">{error}</p>}
      {orders.length === 0 ? (
        <p className="no-orders">You have not placed any orders yet.</p>
      ) : (
        <table className="order-table">
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Products</th>
              <th>Total</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id}>
                <td>{order.id}</td>
                <td>
                  <ul>
                    {order.items.map((item) => (
                      <li key={item.fabric.id}>
                        {item.quantity} X {item.fabric.name} (€{item.pricePerUnit}) = €{item.totalPrice.toFixed(2)}
                      </li>
                    ))}
                  </ul>
                </td>
                <td>€{order.totalAmount.toFixed(2)}</td>
                <td>{order.status}</td>
                <td>{new Date(order.orderDate).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default UserOrdersPage;