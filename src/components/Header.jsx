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
        <Link to="/products">Products</Link>
        <Link to="/aboutus">About Us</Link>
        <Link to="/login">Login</Link>
      </nav>
    </header>
  );
};

export default Header;