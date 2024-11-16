import React, { useState, useEffect } from 'react';
import './EditFabric.css';

const EditFabric = ({ product, onUpdate, onClose }) => {
  const initialFormData = {
    name: product.name,
    material: product.material,
    color: product.color,
    description: product.description,
    price: product.price,
    washable: product.washable,
    ironed: product.ironed,
    stock: product.stock,
    pictureUrl: product.pictureUrl || ''
  };

  const [formData, setFormData] = useState(initialFormData);

  useEffect(() => {
    setFormData(initialFormData);
  }, [product]);

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (isNaN(formData.price) || formData.price <= 0) {
      alert('Please enter a valid price.');
      return;
    }

    if (isNaN(formData.stock) || formData.stock < 0) {
      alert('Please enter a valid stock quantity.');
      return;
    }

    if (
      formData.pictureUrl &&
      !/^(https?:\/\/.*\.(?:png|jpg|jpeg|svg|gif))$/.test(formData.pictureUrl)
    ) {
      alert('Please enter a valid image URL (e.g., http://example.com/image.jpg).');
      return;
    }

    const updatedProduct = {
      ...product,
      ...formData,
      price: parseFloat(formData.price),
      stock: parseInt(formData.stock, 10),
    };

    onUpdate(updatedProduct);
    onClose();
  };

  const handleCancel = () => {
    setFormData(initialFormData);
    onClose();
  };

  return (
    <div className="edit-product-form">
      <h3>Edit Fabric</h3>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          placeholder="Name"
          required
        />

        <textarea
          name="description"
          value={formData.description}
          onChange={handleInputChange}
          placeholder="Description"
          required
        />

        <select
          name="material"
          value={formData.material}
          onChange={handleInputChange}
          required>
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

        <select
          name="color"
          value={formData.color}
          onChange={handleInputChange}
          required>
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
          name="price"
          value={formData.price}
          onChange={handleInputChange}
          placeholder="Price"
          required
        />

        <input
          type="number"
          name="stock"
          value={formData.stock}
          onChange={handleInputChange}
          placeholder="Stock"
          required
        />

        <input
          type="text"
          name="pictureUrl"
          value={formData.pictureUrl}
          onChange={handleInputChange}
          placeholder="Picture URL (optional)"
        />

        <div className="checkbox-group">
          <label>
            <input
              type="checkbox"
              name="washable"
              checked={formData.washable}
              onChange={handleInputChange}
            />
            Washable
          </label>
        </div>

        <div className="checkbox-group">
          <label>
            <input
              type="checkbox"
              name="ironed"
              checked={formData.ironed}
              onChange={handleInputChange}
            />
            Ironed
          </label>
        </div>

        <button type="submit">Update</button>
        <button type="button" onClick={handleCancel}>Cancel</button>
      </form>
    </div>
  );
};

export default EditFabric;