import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./Header.css";
import TokenManager from "../services/tokenManager";

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    setIsLoggedIn(!!TokenManager.getAccessToken());
  }, []);

  const handleLogout = () => {
    TokenManager.clear();
    setIsLoggedIn(false);
  };

  return (
    <header>
      <Link to="/" className="header-title">
        <h1>TrendTrack</h1>
      </Link>
      <nav>
        <Link to="/fabric-management">Fabric Management</Link>
        <Link to="/user-management">User Management</Link>
        <Link to="/orders">Orders</Link>
        <Link to="/statistics">Statistics</Link>
        <Link to="/catalogue">Catalogue</Link>
        <Link to="/cart">Cart</Link>
      </nav>
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