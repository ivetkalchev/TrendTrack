import React from 'react';
import './Footer.css';
import { FaFacebookF, FaInstagram, FaTwitter } from 'react-icons/fa'; 

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-left">
        <p>© 2024 TrendTrack. All rights reserved.</p>
      </div>
      <div className="footer-right">
        <a href="https://www.facebook.com" target="_blank" rel="noreferrer" aria-label="Facebook">
          <FaFacebookF />
        </a>
        <a href="https://www.instagram.com" target="_blank" rel="noreferrer" aria-label="Instagram">
          <FaInstagram />
        </a>
        <a href="https://www.twitter.com" target="_blank" rel="noreferrer" aria-label="Twitter">
          <FaTwitter />
        </a>
      </div>
    </footer>
  );
};

export default Footer;