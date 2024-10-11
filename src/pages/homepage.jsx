import React from 'react';
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-container">

      <section className="about-section">
        <h2>Welcome to TrendTrack!</h2>
        <p>
          At <strong>TrendTrack</strong>, we believe that fashion isn’t just about what you wear—it's a vibrant form of 
          <strong> self-expression</strong>! 🌟 Our carefully curated collections of fabulous fabrics, stunning trims, 
          and chic accessories are here to spark your creativity and elevate your wardrobe to new heights. 
        </p>
        <p>
          Whether you’re a visionary designer, a crafty seamstress, or simply a fashion enthusiast, <strong>TrendTrack </strong> 
          has everything you need to turn your dreams into reality. 🎨✨ Let’s create something magical together!
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
