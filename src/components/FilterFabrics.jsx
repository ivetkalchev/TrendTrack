import React, { useState } from 'react';
import './FilterFabrics.css';

const FilterFabrics = ({ onFilter }) => {
  const [name, setName] = useState('');
  const [material, setMaterial] = useState('');
  const [color, setColor] = useState('');
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(100);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(9);

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilter({
      name,
      material,
      color,
      minPrice,
      maxPrice,
      page,
      size
    });
  };

  return (
    <form onSubmit={handleSubmit} className="filter-form">
      <input
        type="text"
        placeholder="Search by name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <select value={material} onChange={(e) => setMaterial(e.target.value)}>
        <option value="">All Materials</option>
        <option value="COTTON">Cotton</option>
        <option value="POLYESTER">Polyester</option>
        <option value="SILK">Silk</option>
        <option value="WOOL">Wool</option>
        <option value="LINEN">Linen</option>
        <option value="LEATHER">Leather</option>
        <option value="DENIM">Denim</option>
        <option value="NYLON">Nylon</option>
        <option value="SATIN">Satin</option>
        <option value="VELVET">Valvet</option>
      </select>
      <select value={color} onChange={(e) => setColor(e.target.value)}>
        <option value="">All Colors</option>
        <option value="RED">Red</option>
        <option value="BLUE">Blue</option>
        <option value="GREEN">Green</option>
        <option value="BLACK">Black</option>
        <option value="WHITE">White</option>
        <option value="YELLOW">Yellow</option>
        <option value="ORANGE">Orange</option>
        <option value="PURPLE">Purple</option>
        <option value="PINK">Pink</option>
        <option value="BROWN">Brown</option>
        <option value="GREY">Grey</option>
      </select>

      <div className="price-range">
        <label>Price Range: €{minPrice} - €{maxPrice}</label>
        <input
          type="range"
          min="0"
          max="100"
          value={minPrice}
          onChange={(e) => setMinPrice(e.target.value)}
          className="slider"
        />
        <input
          type="range"
          min="0"
          max="100"
          value={maxPrice}
          onChange={(e) => setMaxPrice(e.target.value)}
          className="slider"
        />
      </div>

      <button type="submit">Filter</button>
    </form>
  );
};

export default FilterFabrics;