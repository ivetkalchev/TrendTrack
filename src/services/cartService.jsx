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
      { fabricId, quantity }, // Sending payload as the request body
      {
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
      { fabricId, quantity }, // Sending payload as the request body
      {
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
    const response = await axios.delete(
      `${BASE_URL}/cart/${userId}/remove`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        params: { fabricId }, // Pass as query parameter
      }
    );
    return response.data;
  },
};

export default cartService;