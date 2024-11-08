import React, { useState } from 'react';
import './EditProduct.css';

const EditProduct = ({ product, onUpdate, onClose }) => {
  const [name, setName] = useState(product.name);
  const [material, setMaterial] = useState(product.material);
  const [color, setColor] = useState(product.color);
  const [description, setDescription] = useState(product.description);
  const [price, setPrice] = useState(product.price);
  const [washable, setWashable] = useState(product.washable);
  const [ironed, setIroned] = useState(product.ironed);
  const [stock, setStock] = useState(product.stock);

  const handleSubmit = (e) => {
    e.preventDefault();
    onUpdate({
      ...product,
      name,
      material,
      color,
      description,
      price: parseFloat(price),
      washable,
      ironed,
      stock: parseInt(stock, 10)
    });
    onClose();
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

        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Description"
          required
        />

        <select value={material} onChange={(e) => setMaterial(e.target.value)} required>
          <option value="">Select Material</option>
          <option value="COTTON">Cotton</option>
          <option value="WOOL">Wool</option>
          <option value="LEATHER">Leather</option>
          <option value="SILK">Silk</option>
          <option value="DENIM">Denim</option>
          <option value="POLYESTER">Polyester</option>
        </select>

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

        <div className="checkbox-group">
          <label>
            <input
              type="checkbox"
              checked={washable}
              onChange={(e) => setWashable(e.target.checked)}
            />
            Washable
          </label>
        </div>

        <div className="checkbox-group">
          <label>
            <input
              type="checkbox"
              checked={ironed}
              onChange={(e) => setIroned(e.target.checked)}
            />
            Ironed
          </label>
        </div>

        <input
          type="number"
          value={stock}
          onChange={(e) => setStock(e.target.value)}
          placeholder="Stock"
          required
        />

        <button type="submit">Update</button>
        <button type="button" onClick={onClose}>Cancel</button>
      </form>
    </div>
  );
};

export default EditProduct;