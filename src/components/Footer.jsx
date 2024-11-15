import React from 'react';
import './Footer.css';
import { FaInstagram, FaLinkedin, FaTimes } from 'react-icons/fa'; 
import { FiSearch } from 'react-icons/fi';
import needleIcon from "../assets/img/needle.png";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-header">
          <div className="header-text">
            <h2>Wish to Contact Us?</h2>
            <p className="footer-tagline">Silky flow filled with fashion</p>
          </div>
          <img src={needleIcon} alt="Needle icon" className="needle-icon" />
        </div>
        <hr className="footer-divider" />
        <div className="footer-details">
          <div className="footer-contact">
            <p>Eindhoven, Netherlands</p>
            <a href="tel:+000000000">+00 000 0000 000</a>
          </div>
          <div className="footer-social">
            <a href="https://www.instagram.com" target="_blank" rel="noreferrer" aria-label="Instagram">
              <FaInstagram />
            </a>
            <a href="https://www.linkedin.com" target="_blank" rel="noreferrer" aria-label="LinkedIn">
              <FaLinkedin />
            </a>
            <a href="#" aria-label="Close">
              <FaTimes />
            </a>
          </div>
        </div>
        <p className="footer-copyright">© 2024 TrendTrack. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;