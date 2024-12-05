import axios from "axios";
import BASE_URL from "./config";

export const getFabrics = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/fabrics`);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch fabrics: ${error.message}`);
  }
};

export const addFabric = async (fabric) => {
  try {
    const response = await axios.post(`${BASE_URL}/fabrics`, fabric);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to add fabric: ${error.message}`);
  }
};

export const updateFabric = async (fabric) => {
  try {
    const response = await axios.put(`${BASE_URL}/fabrics/${fabric.id}`, fabric);
    
    return response.data;
  } catch (error) {
    throw new Error(`Failed to update fabric: ${error.message}`);
  }
};

export const deleteFabric = async (id) => {
  try {
    await axios.delete(`${BASE_URL}/fabrics/${id}`);
  } catch (error) {
    throw new Error(`Failed to delete fabric: ${error.message}`);
  }
};