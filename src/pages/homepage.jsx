import React from 'react';
import './HomePage.css';
import mouseIcon from "../assets/img/mouse.png";
import fabricImage from "../assets/img/fabric.png"; // Add the fabric image here
import scissorsIcon from "../assets/img/scissors.png"; // Add the scissors icon here

const HomePage = () => {
  return (
    <div className="home-container">
      <section className="hero-section">
        <h1>A Vibrant Form of Self Expression</h1>
        <p>(scroll)</p>
        <a href="#about" className="scroll-icon">
          <img src={mouseIcon} alt="Scroll Icon" />
        </a>
      </section>
      <section id="about" className="about-section">
        <div className="about-header" >
          <h2>About Us</h2>
          <img src={scissorsIcon} alt="Scissors Icon" className="scissors-icon" />
        </div>
        <div className="about-content">
          <div className="about-text">
            <p>
              At TrendTrack, we believe that fashion isn’t just about what you wear—it’s a vibrant form of 
              self-expression!
            </p>
            <p>
              Our carefully curated collections of fabulous fabrics, stunning trims, and chic accessories are here to spark your creativity 
              and elevate your wardrobe to new heights.
            </p>
            <p>
              Whether you’re a visionary designer, a crafty seamstress, or simply a fashion enthusiast, TrendTrack
              has everything you need to turn your dreams into reality.
            </p>
            <a href="/catalogue" className="browse-button">Browse Catalogue +</a>
          </div>
          <div className="about-image">
            <img src={fabricImage} alt="Fabric" />
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;