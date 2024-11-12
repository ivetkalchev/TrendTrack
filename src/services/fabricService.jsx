import axios from 'axios';

const BASE_URL = 'http://localhost:8080/fabrics';

export const getFabrics = async () => {
  try {
    const response = await axios.get(BASE_URL);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch fabrics: ${error.message}`);
  }
};

export const addFabric = async (fabric) => {
  try {
    const response = await axios.post(BASE_URL, fabric);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to add fabric: ${error.message}`);
  }
};

export const updateFabric = async (fabric) => {
  try {
    const response = await axios.put(`${BASE_URL}/${fabric.id}`, fabric);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to update fabric: ${error.message}`);
  }
};

export const deleteFabric = async (id) => {
  try {
    await axios.delete(`${BASE_URL}/${id}`);
  } catch (error) {
    throw new Error(`Failed to delete fabric: ${error.message}`);
  }
};