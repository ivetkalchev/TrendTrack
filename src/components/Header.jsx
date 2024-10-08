import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = () => {
  return (
    <header>
      <Link to="/" style={{ textDecoration: 'none', color: 'inherit' }}> {}
        <h1>TrendTrack</h1>
      </Link>
      <nav>
        <Link to="/products">Products</Link>
        {}
        <Link to="/aboutus">About Us</Link>
        {}  
      </nav>
    </header>
  );
};

export default Header;
