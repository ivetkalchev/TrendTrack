import React from 'react';
import './HomePage.css';
import mouseIcon from "../assets/img/mouse.png";

const HomePage = () => {
  return (
    <div className="home-container">
      <section className="hero-section">
        <h1>A Vibrant Form of Self Expression</h1>
        <p>(scroll)</p>
        <div className="scroll-icon">
          <img src={mouseIcon} alt="Scroll Icon" />
        </div>
      </section>
      <section className="about-section">
        <h2>About Us</h2>
        <p>
          At <strong>TrendTrack</strong>, we believe that fashion isn’t just about what you wear—it’s a vibrant form of 
          <strong> self-expression</strong>!
        </p>
        <p>
          Our carefully curated collections of fabulous fabrics, stunning trims, and chic accessories are here to spark your creativity 
          and elevate your wardrobe to new heights.
        </p>
        <p>
          Whether you’re a visionary designer, a crafty seamstress, or simply a fashion enthusiast, <strong>TrendTrack</strong> 
          has everything you need to turn your dreams into reality.
        </p>
        <a href="/catalogue" className="browse-button">Browse Catalogue +</a>
      </section>
    </div>
  );
};

export default HomePage;