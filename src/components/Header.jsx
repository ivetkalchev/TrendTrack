import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./Header.css";
import TokenManager from "../services/tokenManager";

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState(null);

  useEffect(() => {
    const accessToken = TokenManager.getAccessToken();
    setIsLoggedIn(!!accessToken);
    if (accessToken) {
      const claims = TokenManager.getClaims();
      console.log("Claims:", claims); // Debugging line
      setRole(claims?.roles?.[0] || null);
    }
  }, []);

  const handleLogout = () => {
    TokenManager.clear();
    setIsLoggedIn(false);
    setRole(null);
  };

  const renderLinks = () => {
    if (!isLoggedIn) {
      return (
        <>
          <Link to="/catalogue">Catalogue</Link>
        </>
      );
    }

    if (role === "ADMIN") {
      return (
        <>
          <Link to="/fabric-management">Fabric Management</Link>
          <Link to="/user-management">User Management</Link>
          <Link to="/orders">Orders</Link>
          <Link to="/statistics">Statistics</Link>
          <Link to="/my-account">My Account</Link>
        </>
      );
    }

    if (role === "CLIENT") {
      return (
        <>
          <Link to="/catalogue">Catalogue</Link>
          <Link to="/cart">Cart</Link>
          <Link to="/my-account">My Account</Link>
          <Link to="/my-orders">My Orders</Link>
        </>
      );
    }

    return null;
  };

  return (
    <header>
      <Link to="/" className="header-title">
        <h1>TrendTrack</h1>
      </Link>
      <nav>{renderLinks()}</nav>
      {isLoggedIn ? (
        <button onClick={handleLogout} className="logout-button">
          Logout
        </button>
      ) : (
        <Link to="/authentication" className="login-button">
          Login
        </Link>
      )}
    </header>
  );
};

export default Header;