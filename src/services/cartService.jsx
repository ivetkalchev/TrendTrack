import axios from "axios";
import TokenManager from "./tokenManager";
import BASE_URL from "./config";

const cartService = {
  getCart: async () => {
    const token = TokenManager.getAccessToken();
    const userId = TokenManager.getUserIdFromToken();
    if (!userId || !token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.get(`${BASE_URL}/cart/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  },

  addToCart: async (fabricId, quantity) => {
    const token = TokenManager.getAccessToken();
    const userId = TokenManager.getUserIdFromToken();
    if (!userId || !token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.post(
      `${BASE_URL}/cart/${userId}/add`,
      null, // No body required
      {
        params: { fabricId, quantity },
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );    
    return response.data;
  },

  updateCartItem: async (fabricId, quantity) => {
    const token = TokenManager.getAccessToken();
    const userId = TokenManager.getUserIdFromToken();
    if (!userId || !token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.put(
      `${BASE_URL}/cart/${userId}/update`,
      null, // No body is required
      {
        params: { fabricId, quantity },
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  },

  removeFromCart: async (fabricId) => {
    const token = TokenManager.getAccessToken();
    const userId = TokenManager.getUserIdFromToken();
    if (!userId || !token) {
      throw new Error("User is not authenticated.");
    }
    const response = await axios.delete(`${BASE_URL}/cart/${userId}/remove`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      data: { fabricId }, // DELETE requests require `data` for the body
    });
    return response.data;
  },
};

export default cartService;