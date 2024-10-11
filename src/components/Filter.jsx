import React, { useState } from 'react';
import './Filter.css';

const FilterComponent = ({ onFilter }) => {
  const [name, setName] = useState('');
  const [color, setColor] = useState('');

  const handleNameChange = (e) => {
    setName(e.target.value);
    onFilter(e.target.value, color);
  };

  const handleColorChange = (e) => {
    setColor(e.target.value);
    onFilter(name, e.target.value);
  };

  const handleClearFilters = () => {
    setName('');
    setColor('');
    onFilter('', '');
  };

  return (
    <div className="filter-container">
      <input
        type="text"
        placeholder="Search by name..."
        value={name}
        onChange={handleNameChange}
        className="filter-input"
      />
      <select value={color} onChange={handleColorChange} className="filter-select">
        <option value="">All Colors</option>
        <option value="RED">Red</option>
        <option value="GREEN">Green</option>
        <option value="BLUE">Blue</option>
        <option value="YELLOW">Yellow</option>
        <option value="BLACK">Black</option>
        <option value="WHITE">White</option>
        <option value="PURPLE">Purple</option>
        <option value="ORANGE">Orange</option>
        <option value="PINK">Pink</option>
        <option value="BROWN">Brown</option>
        <option value="GREY">Grey</option>
        <option value="BEIGE">Beige</option>
      </select>
      <button onClick={handleClearFilters} className="clear-button">Clear Filters</button>
    </div>
  );
};

export default FilterComponent;