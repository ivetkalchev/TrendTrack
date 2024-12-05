import axios from "axios";
import BASE_URL from "./config";

export const getAllUsers = async () => {
  const response = await axios.get(`${BASE_URL}/users`, {
    headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
  });
  return response.data;
};

export const deleteUser = async (id) => {
  await axios.delete(`${BASE_URL}/users/${id}`, {
    headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
  });
};

export const promoteToAdmin = async (id) => {
  await axios.put(`${BASE_URL}/users/${id}/promote`, null, {
    headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
  });
};