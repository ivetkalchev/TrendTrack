import axios from "axios";
import TokenManager from "./tokenManager";
import BASE_URL from "./config";

const orderService = {
  createOrder: async (orderData) => {
    const token = TokenManager.getAccessToken();
    if (!token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.post(`${BASE_URL}/orders`, orderData, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },

  getAllOrders: async () => {
    const token = TokenManager.getAccessToken();
    if (!token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.get(`${BASE_URL}/orders`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },

  getOrderById: async (orderId) => {
    const token = TokenManager.getAccessToken();
    if (!token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.get(`${BASE_URL}/orders/${orderId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },

  updateOrder: async (orderId, updateData) => {
    const token = TokenManager.getAccessToken();
    if (!token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.put(`${BASE_URL}/orders`, { id: orderId, ...updateData }, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },

  deleteOrder: async (orderId) => {
    const token = TokenManager.getAccessToken();
    if (!token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.delete(`${BASE_URL}/orders/${orderId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },

  getOrdersByUserId: async () => {
    const token = TokenManager.getAccessToken();
    const userId = TokenManager.getUserIdFromToken();
    if (!userId || !token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.get(`${BASE_URL}/orders/users/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },
};

export default orderService;