import axios from "axios";
import BASE_URL from "./config";
import TokenManager from "../services/tokenManager";

const getToken = () => localStorage.getItem("accessToken");

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

export const deleteUser = async (id) => {
  try {
    console.log("Attempting to delete user with ID:", id);
    await axios.delete(`${BASE_URL}/users/${id}`, {
      headers: {
        Authorization: `Bearer ${getToken()}`,
        "Content-Type": "application/json",
      },
    });
    console.log("User deleted successfully.");
  } catch (error) {
    console.error("Error while deleting user:", error.response || error.message);
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to delete user: ${error.response?.data?.message || error.message}`);
  }
};

export const editUser = async (id, user, token) => {
  try {
    console.log("Attempting to edit user with ID:", id);
    await axios.put(
      `${BASE_URL}/users/${id}`,
      user, 
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
    console.log("User edited successfully.");
  } catch (error) {
    console.error("Error while editing user:", error.response || error.message);
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to edit user: ${error.response?.data?.message || error.message}`);
  }
};

export const getUserDetailsById = async (id) => {
  console.log(`Fetching details for user ID: ${id}`);
  try {
    const response = await axios.get(`${BASE_URL}/users/${id}`, {
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    });
    console.log("Fetched User Details:", response.data);
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      TokenManager.clear();
      window.location.href = "/login";
    }
    console.error("Error fetching user details:", error);
    throw new Error(`Failed to fetch user details: ${error.message}`);
  }
};
