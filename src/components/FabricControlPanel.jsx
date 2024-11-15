import React, { useState } from 'react';
import './FabricControlPanel.css';
import EditProductForm from './EditFabric';
import { FaEdit, FaTrash } from 'react-icons/fa';
import Swal from 'sweetalert2';

const FabricControlPanel = ({ product, onDelete, onUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);

  const handleDelete = () => {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!"
    }).then((result) => {
      if (result.isConfirmed) {
        onDelete(product.id);
        Swal.fire({
          title: "Deleted!",
          text: "Your fabric has been deleted.",
          icon: "success"
        });
      }
    });
  };

  const handleEdit = (updatedProduct) => {
    onUpdate(updatedProduct);
    setIsEditing(false);
  };

  return (
    <div className="product-admin-item">
      <div className="product-header">
        <h3 className="product-title">{product.name}</h3>
        <div className="button-container">
          <button onClick={() => setIsEditing(true)} className="edit-button">
            <FaEdit />
          </button>
          <button onClick={handleDelete} className="delete-button">
            <FaTrash />
          </button>
        </div>
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