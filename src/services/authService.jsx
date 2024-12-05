import axios from "axios";
import BASE_URL from "./config";

export const login = async (credentials) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/tokens/login`, credentials,
      {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      }
    );
    return response.data;c
  } catch (error) {
    throw new Error(`Failed to login: ${error.message}`);
  }
};

export const register = async (userData) => {
  try {
    const response = await axios.post(`${BASE_URL}/users`, userData, {
      headers: { "Content-Type": "application/json" },
    });
    return response.data;
  } catch (error) {
    throw new Error(`Failed to register: ${error.message}`);
  }
};