import React, { useState } from "react";
import "./OrderFilter.css";

const OrderFilter = ({ onFilter }) => {
  const [id, setId] = useState("");
  const [orderDate, setOrderDate] = useState("");
  const [status, setStatus] = useState("");
  const [page] = useState(0);
  const [size] = useState(9);

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilter({
      id,
      orderDate,
      status,
      page,
      size
    });
  };
  return (
    <form onSubmit={handleSubmit} className="filter-order-form">
      <div>
        <label>Order ID</label>
        <input
          type="number"
          value={id}
          onChange={(e) => setId(e.target.value)}
        />
      </div>
      <div>
        <label>Order Date</label>
        <input
          type="date"
          value={orderDate}
          onChange={(e) => setOrderDate(e.target.value)}
        />
      </div>
      <div>
        <label>Status</label>
        <select value={status} onChange={(e) => setStatus(e.target.value)}>
          <option value="">All</option>
          <option value="PENDING">Pending</option>
          <option value="SHIPPED">Shipped</option>
          <option value="DELIVERED">Delivered</option>
        </select>
      </div>
      <button type="submit">Filter</button>
    </form>
  );
};

export default OrderFilter;