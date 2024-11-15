import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = () => {
  return (
    <header>
      <Link to="/" className="header-title">
        <h1>TrendTrack</h1>
      </Link>
      <nav>
        <Link to="/fabric-management">Fabric Managment</Link>
        <Link to="/catalogue">Catalogue</Link>
        <Link to="/cart">Cart</Link>
      </nav>
      <Link to="/login" className="login-button">Login</Link>
    </header>
  );
};

export default Header;