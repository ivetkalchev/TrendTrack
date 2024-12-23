import axios from "axios";
import BASE_URL from "./config";
import TokenManager from "./tokenManager";

export const addFabric = async (fabric) => {
  const token = TokenManager.getAccessToken();

  if (!token) {
    throw new Error("Authorization token is missing");
  }

  try {
    const response = await axios.post(
      `${BASE_URL}/fabrics`, 
      fabric,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    throw new Error(`Failed to add fabric: ${error.message}`);
  }
};


export const getFabrics = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/fabrics`);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch fabrics: ${error.message}`);
  }
};

export const updateFabric = async (fabric) => {
  const token = TokenManager.getAccessToken();

  try {
    const response = await axios.put(
      `${BASE_URL}/fabrics/${fabric.id}`,
      fabric,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    throw new Error(`Failed to update fabric: ${error.message}`);
  }
};

export const deleteFabric = async (id, token) => {
  try {
    await axios.delete(`${BASE_URL}/fabrics/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    throw new Error(`Failed to delete fabric: ${error.message}`);
  }
};