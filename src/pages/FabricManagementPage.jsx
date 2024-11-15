import React, { useEffect, useState } from 'react';
import { getFabrics, addFabric, updateFabric, deleteFabric } from '../services/fabricService';
import ProductAdmin from '../components/FabricControlPanel';
import AddProduct from '../components/AddFabric';
import './FabricManagementPage.css';
import { FaPlus } from 'react-icons/fa'; 

const ProductManagementPage = () => {
  const [fabrics, setFabrics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isAdding, setIsAdding] = useState(false);

  useEffect(() => {
    fetchFabrics();
  }, []);

  const fetchFabrics = async () => {
    setLoading(true);
    setError(null);

    try {
      const data = await getFabrics();
      setFabrics(data.fabrics);
    } catch (err) {
      setError('Failed to fetch fabrics. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = async (newFabric) => {
    setError(null);
    try {
      const addedFabric = await addFabric(newFabric);
      setFabrics((prevFabrics) => [...prevFabrics, addedFabric]);
    } catch (err) {
      setError('Failed to add fabric. Please try again.');
    }
  };

  const handleDelete = async (id) => {
    setError(null);
    try {
      await deleteFabric(id);
      setFabrics((prevFabrics) => prevFabrics.filter((fabric) => fabric.id !== id));
    } catch (err) {
      setError('Failed to delete fabric. Please try again.');
    }
  };

  const handleUpdate = async (updatedFabric) => {
    setError(null);
    try {
      await updateFabric(updatedFabric);
      fetchFabrics();
    } catch (err) {
      setError('Failed to update fabric. Please try again.');
    }
  };       

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="product-management-container">
      <div className="header-container">
        <h2>Fabric Management</h2>
        <button onClick={() => setIsAdding(true)} className="add-product-button">
          <FaPlus />
        </button>
      </div>

      {isAdding && (
        <AddProduct
          onAdd={handleAdd}
          onClose={() => setIsAdding(false)}
        />
      )}

      {fabrics.length === 0 ? (
        <p>No results match your search.</p>
      ) : (
        <div className="product-list">
          {fabrics.map((fabric) => (
            <ProductAdmin
              key={`${fabric.id}`}
              product={fabric}
              onDelete={handleDelete}
              onUpdate={handleUpdate}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductManagementPage;