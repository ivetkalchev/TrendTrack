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
        if (!token) {
          return false;
        }
    
        const claims = TokenManager.getClaims();
        if (claims && claims.exp) {
          const currentTime = Math.floor(Date.now() / 1000);
          if (claims.exp < currentTime) {
            TokenManager.clear(); 
            return false;
          }
        }
        return true;
      }
}
 
export default TokenManager;