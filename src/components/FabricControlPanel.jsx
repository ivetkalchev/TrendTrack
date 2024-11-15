import React, { useState } from 'react';
import './FabricControlPanel.css';
import EditProductForm from './EditFabric';
import { FaEdit, FaTrash } from 'react-icons/fa';

const FabricControlPanel = ({ product, onDelete, onUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);

  const handleDelete = () => {
    const confirmDelete = window.confirm('Are you sure you want to delete this fabric?');
    if (confirmDelete) {
      onDelete(product.id);
    }
  };

  const handleEdit = (updatedProduct) => {
    onUpdate(updatedProduct);
    setIsEditing(false);
  };

  return (
    <div className="product-admin-item">
      <h3 className="product-title">{product.name}</h3>
      <div className="button-container">
        <button onClick={() => setIsEditing(true)} className="edit-button">
          <FaEdit /> Edit
        </button>
        <button onClick={handleDelete} className="delete-button">
          <FaTrash /> Delete
        </button>
      </div>
      {isEditing && (
        <EditProductForm
          product={product}
          onUpdate={handleEdit}
          onClose={() => setIsEditing(false)}
        />
      )}
    </div>
  );
};

export default FabricControlPanel;