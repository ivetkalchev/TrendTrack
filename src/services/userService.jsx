import axios from "axios";
import BASE_URL from "./config";

const getToken = () => localStorage.getItem("accessToken");

// Get all users
export const getAllUsers = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/users`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    });
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to fetch users: ${error.message}`);
  }
};

// Delete user by ID
export const deleteUser = async (id) => {
  try {
    await axios.delete(`${BASE_URL}/users/${id}`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    });
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to delete user: ${error.message}`);
  }
};

// Promote user to admin by ID
export const promoteToAdmin = async (id) => {
  try {
    await axios.put(`${BASE_URL}/users/${id}/promote`, null, {
      headers: { Authorization: `Bearer ${getToken()}` },
    });
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to promote user: ${error.message}`);
  }
};