import React, { useState } from 'react';
import './AddProduct.css';

const AddProduct = ({ onAdd, onClose }) => {
  const [name, setName] = useState('');
  const [color, setColor] = useState('');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [description, setDescription] = useState(''); // New state for description

  const handleSubmit = (e) => {
    e.preventDefault();
    const newProduct = {
      name,
      color,
      price: parseFloat(price),
      quantity: parseInt(quantity, 10),
      description, // Include description in the new product object
    };
    onAdd(newProduct); // Call to handle adding the new product
    onClose(); // Close the form
  };

  return (
    <div className="add-product-form">
      <h3>Add Product</h3>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Product Name"
          required
        />
        <textarea
          value={description} // Bind description state
          onChange={(e) => setDescription(e.target.value)} // Update description state
          placeholder="Description"
          required
        />
        <select value={color} onChange={(e) => setColor(e.target.value)} required>
          <option value="">Select Color</option>
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
        <input
          type="number"
          value={price}
          onChange={(e) => setPrice(e.target.value)}
          placeholder="Price"
          required
        />
        <input
          type="number"
          value={quantity}
          onChange={(e) => setQuantity(e.target.value)}
          placeholder="Quantity"
          required
        />
        <button type="submit">Add</button>
        <button type="button" onClick={onClose}>Cancel</button>
      </form>
    </div>
  );
};

export default AddProduct;