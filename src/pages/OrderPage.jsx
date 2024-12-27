import React, { useEffect, useState } from "react";
import orderService from "../services/orderService";
import { FaTrash } from "react-icons/fa";
import "./OrderPage.css";

const OrderPage = () => {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await orderService.getAllOrders();
        setOrders(data.orders);
      } catch (err) {
        console.error("Error fetching orders:", err.message);
        setError(err.message);
      }
    };

    fetchOrders();
  }, []);

  const handleStatusChange = async (orderId, status) => {
    try {
      await orderService.updateOrder(orderId, { status });
      setOrders((prevOrders) =>
        prevOrders.map((order) =>
          order.id === orderId ? { ...order, status } : order
        )
      );
    } catch (err) {
      console.error("Error updating status:", err.message);
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };

  const handleDeleteOrder = async (orderId) => {
    try {
      await orderService.deleteOrder(orderId);
      setOrders((prevOrders) => prevOrders.filter((order) => order.id !== orderId));
    } catch (err) {
      console.error("Error deleting order:", err.message);
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };

  if (error) {
    return <div className="inline-error">There was an error: {error}</div>;
  }

  return (
    <div className="order-page">
      <h1>Orders</h1>
      {orders.length === 0 ? (
        <p className="no-orders">No orders available.</p>
      ) : (
        <table className="order-table">
          <thead>
            <tr>
              <th>User</th>
              <th>Products</th>
              <th>Total</th>
              <th>Address</th>
              <th>Status</th>
              <th>Date</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id}>
                <td>{order.user.username}</td>
                <td>
                  <ul>
                    {order.items.map((item) => (
                      <li key={item.fabric.id}>
                        {item.quantity} X {item.fabric.name} (€{item.pricePerUnit}) = €{item.totalPrice.toFixed(2)}
                      </li>
                    ))}
                  </ul>
                </td>
                <td>€{order.totalAmount}</td>
                <td>{order.address}</td>
                <td>
                  <select
                    value={order.status}
                    onChange={(e) => handleStatusChange(order.id, e.target.value)}
                  >
                    <option value="PENDING">PENDING</option>
                    <option value="SHIPPED">SHIPPED</option>
                    <option value="DELIVERED">DELIVERED</option>
                  </select>
                </td>
                <td>{new Date(order.orderDate).toLocaleDateString()}</td>
                <td>
                  <button
                    className="delete-btn"
                    onClick={() => handleDeleteOrder(order.id)}
                  >
                    <FaTrash />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default OrderPage;