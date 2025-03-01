import { jwtDecode } from "jwt-decode";
 
const TokenManager = {
  getAccessToken: () => localStorage.getItem("accessToken"),

  getClaims: () => {
    if (!localStorage.getItem("claims")) {
      return undefined;
    }
    return JSON.parse(localStorage.getItem("claims"));
  },

  setAccessToken: (token) => {
    localStorage.setItem("accessToken", token);
    const claims = jwtDecode(token);
    localStorage.setItem("claims", JSON.stringify(claims));
    return claims;
  },

  clear: () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("claims");
  },

  getUserIdFromToken: () => {
    const claims = TokenManager.getClaims();
    if (claims && claims.userId) {
      return claims.userId;
    }
    return null;
  },

  isAuthenticated: () => {
    const token = TokenManager.getAccessToken();
    return !!token;
  },

  getRole: () => {
    const claims = TokenManager.getClaims();
    if (claims && claims.roles && claims.roles.length > 0) {
      return claims.roles[0];
    }
    return null;
  },
};
 
export default TokenManager;