import React from 'react';
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-container">

      <section className="about-section">
        <h2>About TrendTrack</h2>
        <p>
          At TrendTrack, we believe that fashion is not just about clothing—it's a form of self-expression. 
          Our curated collections of fabrics, trims, and accessories are designed to inspire creativity and 
          elevate your wardrobe. Whether you're a designer, a seamstress, or a fashion enthusiast, 
          TrendTrack has everything you need to bring your vision to life.
        </p>
      </section>

      <section className="gallery-section">
        <h2>Our Collection</h2>
        <div className="gallery">
          <img src="/src/assets/img/purple-fabric.webp" alt="Fashion fabric 1" />
          <img src="/src/assets/img/white-fabric.webp" alt="Fashion fabric 2" />
          <img src="/src/assets/img/black-fabric.webp" alt="Fashion fabric 3" />
        </div>
      </section>
    </div>
  );
};

export default HomePage;
