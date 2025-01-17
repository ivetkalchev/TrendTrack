import React from 'react';
import './Footer.css';
import { FaInstagram, FaLinkedin, FaTwitter } from 'react-icons/fa'; 
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
            <a href="https://www.instagram.com" aria-label="Instagram">
              <FaInstagram />
            </a>
            <a href="https://www.linkedin.com" aria-label="LinkedIn">
              <FaLinkedin />
            </a>
            <a href="https://www.twitter.com" aria-label="Twitter">
              <FaTwitter />
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;