const BASE_URL = "http://localhost:8080";
import axios from "axios";
  
export const login = async (credentials) => {
  const response = await axios({method: "post", url: `${BASE_URL}/tokens/login`, data:credentials});

  if (!response.ok) {
    throw new Error("Failed to login");
  }

  return response.json();
};

export const register = async (userData) => {
  const response = await fetch(`${BASE_URL}/users`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });

  if (!response.ok) {
    throw new Error("Failed to register");
  }

  return response.json();
};