import axios from "axios";
import BASE_URL from "./config";
import TokenManager from "../services/tokenManager";

const getToken = () => localStorage.getItem("accessToken");

export const getAllUsers = async ({ username, firstName, lastName, page, size }) => {
  try {
    const response = await axios.get(`${BASE_URL}/users`, {
      headers: { Authorization: `Bearer ${getToken()}` },
      params: { 
        username, 
        firstName, 
        lastName, 
        page, 
        size 
      },
    });
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
    }
    throw new Error(`Failed to fetch users: ${error.message}`);
  }
};

export const deleteUser = async (id) => {
  try {
    await axios.delete(`${BASE_URL}/users/${id}`, {
      headers: {
        Authorization: `Bearer ${getToken()}`,
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
    }
    throw new Error(`Failed to delete user: ${error.response?.data?.message || error.message}`);
  }
};

export const editUser = async (id, user, token) => {
  try {
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
  } catch (error) {
    console.error("Error while editing user:", error.response || error.message);
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
    }
    throw new Error(`Failed to edit user: ${error.response?.data?.message || error.message}`);
  }
};

export const getUserDetailsById = async (id) => {
  try {
    const response = await axios.get(`${BASE_URL}/users/${id}`, {
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    });
    return response.data;
  } catch (error) {
    if (error.response) {
      /*alert("Unauthorized. Please log in again.");
      TokenManager.clear();
      window.location.href = "/authentication";*/
    }
    console.error("Error fetching user details:", error);
    throw new Error(`Failed to fetch user details: ${error.message}`);
  }
};
