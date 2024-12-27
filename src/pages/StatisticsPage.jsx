import React, { useEffect, useState } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar, Pie } from "react-chartjs-2";
import orderService from "../services/orderService";
import { getAllUsers } from "../services/userService";
import "./StatisticsPage.css";

// Register required components
ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Tooltip, Legend);

const StatisticsPage = () => {
  const [orders, setOrders] = useState([]);
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const ordersData = await orderService.getAllOrders();
        const usersData = await getAllUsers();
        setOrders(ordersData.orders);
        setUsers(usersData.users);
      } catch (err) {
        console.error("Error fetching data:", err.message);
        setError("Failed to load statistics. Please try again.");
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (orders.length > 0 && users.length > 0) {
      calculateStatistics(orders, users);
    }
  }, [orders, users]);

  const calculateStatistics = (orders, users) => {
    const totalClients = users.length;
    const totalOrders = orders.length;
    const ordersToday = orders.filter(
      (order) =>
        new Date(order.orderDate).toDateString() === new Date().toDateString()
    ).length;
    const totalRevenue = orders.reduce((sum, order) => sum + order.totalAmount, 0);
    const ordersByDay = orders.reduce((counts, order) => {
      const date = new Date(order.orderDate).toLocaleDateString();
      counts[date] = (counts[date] || 0) + 1;
      return counts;
    }, {});
    const statusCounts = orders.reduce((counts, order) => {
      counts[order.status] = (counts[order.status] || 0) + 1;
      return counts;
    }, {});

    setStats({
      totalClients,
      totalOrders,
      ordersToday,
      totalRevenue: totalRevenue.toFixed(2),
      ordersByDay,
      statusCounts,
    });
  };

  return (
    <div className="statistics-page">
      <h1>Statistics Overview</h1>
      {error && <p className="error-message">{error}</p>}
      {!stats ? (
        <p>Loading statistics...</p>
      ) : (
        <div className="stats-container">
          <div className="stat">
            <h3>Total Clients</h3>
            <p>{stats.totalClients}</p>
          </div>
          <div className="stat">
            <h3>Total Orders</h3>
            <p>{stats.totalOrders}</p>
          </div>
          <div className="stat">
            <h3>Orders Today</h3>
            <p>{stats.ordersToday}</p>
          </div>
          <div className="stat">
            <h3>Total Revenue</h3>
            <p>€{stats.totalRevenue}</p>
          </div>
          <div className="stat chart">
            <h3>Orders by Day</h3>
            <Bar
              data={{
                labels: Object.keys(stats.ordersByDay),
                datasets: [
                  {
                    label: "Orders",
                    data: Object.values(stats.ordersByDay),
                    backgroundColor: "rgba(75, 192, 192, 0.6)",
                    borderColor: "rgba(75, 192, 192, 1)",
                    borderWidth: 1,
                  },
                ],
              }}
              options={{
                responsive: true,
                scales: {
                  y: {
                    beginAtZero: true,
                  },
                },
              }}
            />
          </div>
          <div className="stat chart">
            <h3>Order Status</h3><br></br>
            <Pie
              data={{
                labels: Object.keys(stats.statusCounts),
                datasets: [
                  {
                    data: Object.values(stats.statusCounts),
                    backgroundColor: [
                      "#FF6384",
                      "#36A2EB",
                      "#FFCE56",
                      "#4BC0C0",
                      "#9966FF",
                    ],
                  },
                ],
              }}
              options={{
                responsive: true,
              }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default StatisticsPage;