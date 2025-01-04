import React, { useEffect, useState } from "react";
import orderService from "../services/orderService";
import { FaTrash } from "react-icons/fa";
import OrderFilter from "../components/OrderFilter";
import "./OrderPage.css";

const OrderPage = () => {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null);
  const [pagination, setPagination] = useState({ page: 0, size: 9, filters: {} });
 const [filters, setFilters] = useState({
    id: "",
    orderDate: "",
    status: ""
    });

    useEffect(() => {
      const fetchOrders = async () => {
        try {
          const data = await orderService.getAllOrders({ ...filters, page: pagination.page, size: pagination.size });
          setOrders(data.orders);
        } catch (error) {
          setError(error.message);
        }
      };
      fetchOrders();
    }, [filters, pagination]);    

  const handleFilter = (filterParams) => {
    setFilters(filterParams);
    setPagination((prevState) => ({
      ...prevState,
      page: 0,
    }));
  };

  const handlePagination = (newPage) => {
    setPagination((prevState) => ({
      ...prevState,
      page: newPage,
    }));
  };

  const hasNextPage = orders.length === pagination.size;
  
  const handleStatusChange = async (orderId, status) => {
    try {
      await orderService.updateOrder(orderId, { status });
      setFilteredOrders((prevOrders) =>
        prevOrders.map((order) =>
          order.id === orderId ? { ...order, status } : order
        )
      );
    } catch (err) {
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };

  const handleDeleteOrder = async (orderId) => {
    try {
      await orderService.deleteOrder(orderId);
      setFilteredOrders((prevOrders) => prevOrders.filter((order) => order.id !== orderId));
    } catch (err) {
      alert(`Error: ${err.response?.data?.detail || err.message}`);
    }
  };

  if (error) {
    return <div className="inline-error">There was an error: {error}</div>;
  }

  return (
    <div className="order-page">
      <h1>Orders</h1>
      <OrderFilter onFilter={handleFilter} />
            {orders.length === 0 ? (
        <p className="no-orders">No orders available.</p>
      ) : (
        <>
          <table className="order-table">
            <thead>
              <tr>
                <th>ID</th>
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
                  <td>{order.id}</td>
                  <td>{order.user.username}</td>
                  <td>
                    <ul>
                      {order.items.map((item) => (
                        <li key={item.fabric.id}>
                          {item.quantity} X {item.fabric.name} (€{item.pricePerUnit}) = €
                          {item.totalPrice.toFixed(2)}
                        </li>
                      ))}
                    </ul>
                  </td>
                  <td>€{order.totalAmount.toFixed(2)}</td>
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
                    <button className="delete-btn" onClick={() => handleDeleteOrder(order.id)}>
                      <FaTrash />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="pagination-controls">
            <button
              onClick={() => handlePagination(pagination.page - 1)}
              disabled={pagination.page === 0}
            >
              Previous
            </button>
            <span>
              Page {pagination.page + 1}
            </span>
            <button
              onClick={() => handlePagination(pagination.page + 1)}
              disabled={!hasNextPage}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default OrderPage;