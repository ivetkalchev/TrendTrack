import React, { useState } from 'react';
import './EditProduct.css';

const EditProduct = ({ product, onUpdate, onClose }) => {
  const [name, setName] = useState(product.name);
  const [color, setColor] = useState(product.color);
  const [price, setPrice] = useState(product.price);
  const [quantity, setQuantity] = useState(product.quantity);

  const handleSubmit = (e) => {
    e.preventDefault();
    onUpdate({ ...product, name, color, price, quantity });
    onClose();
  };

  const handleColorChange = (e) => {
    setColor(e.target.value);
  };

  return (
    <div className="edit-product-form">
      <h3>Edit Product</h3>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Product Name"
          required
        />
        
        <select value={color} onChange={handleColorChange} className="color-select" required>
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
        
        <button type="submit">Update</button>
        <button type="button" onClick={onClose}>Cancel</button>
      </form>
    </div>
  );
};

export default EditProduct;
