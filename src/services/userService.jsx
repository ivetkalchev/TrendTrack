import axios from "axios";
import BASE_URL from "./config";

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

export const promoteToAdmin = async (id) => {
  try {
    await axios.put(`${BASE_URL}/users/${id}/promote`, null, {
      headers: {
        Authorization: `Bearer ${getToken()}`,
        "Content-Type": "application/json",
      },
    });
    return { success: true, message: "User promoted to admin successfully." };
  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to promote user: ${error.response?.data?.message || error.message}`);
  }
};

export const fireEmployee = async (id) => {
  try {
    console.log("Attempting to fire employee with ID:", id);
    const response = await axios.post(
      `${BASE_URL}/users/${id}/fire`,
      null,
      {
        headers: {
          Authorization: `Bearer ${getToken()}`,
          "Content-Type": "application/json",
        },
      }
    );
    console.log("Employee fired successfully.");
    return response.data;
  } catch (error) {
    console.error("Error while firing employee:", error.response || error.message);
    if (error.response && error.response.status === 401) {
      alert("Unauthorized. Please log in again.");
      localStorage.removeItem("accessToken");
    }
    throw new Error(`Failed to fire employee: ${error.response?.data?.message || error.message}`);
  }
};