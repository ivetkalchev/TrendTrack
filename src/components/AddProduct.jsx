import React, { useState } from 'react';
import './AddProduct.css';

const AddProduct = ({ onAdd, onClose }) => {
  const [name, setName] = useState('');
  const [color, setColor] = useState('');
  const [material, setMaterial] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');
  const [washable, setWashable] = useState(false);
  const [ironed, setIroned] = useState(false);
  const [stock, setStock] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    const newProduct = {
      name,
      description,
      material,
      color,
      price: parseFloat(price),
      washable,
      ironed,
      stock: parseInt(stock, 10),
    };
    onAdd(newProduct);
    onClose();
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
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Description"
          required
        />
        <select value={material} onChange={(e) => setMaterial(e.target.value)} required>
          <option value="">Select Material</option>
          <option value="COTTON">Cotton</option>
          <option value="POLYESTER">Polyester</option>
          <option value="SILK">Silk</option>
          <option value="WOOL">Wool</option>
          <option value="LINEN">Linen</option>
          <option value="LEATHER">Leather</option>
          <option value="DENIM">Denim</option>
          <option value="NYLON">Nylon</option>
          <option value="SATIN">Satin</option>
          <option value="VELVET">Velvet</option>
        </select>
        <select value={color} onChange={(e) => setColor(e.target.value)} required>
          <option value="">Select Color</option>
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
        <input
          type="number"
          value={price}
          onChange={(e) => setPrice(e.target.value)}
          placeholder="Price"
          required
        />
        <input
          type="number"
          value={stock}
          onChange={(e) => setStock(e.target.value)}
          placeholder="Stock Quantity"
          required
        />
        <div className="checkbox-container">
          <label>
            <input
              type="checkbox"
              checked={washable}
              onChange={(e) => setWashable(e.target.checked)}
            />
            Washable
          </label>
          <label>
            <input
              type="checkbox"
              checked={ironed}
              onChange={(e) => setIroned(e.target.checked)}
            />
            Ironed
          </label>
        </div>
        <button type="submit">Add</button>
        <button type="button" onClick={onClose}>Cancel</button>
      </form>
    </div>
  );
};

export default AddProduct;