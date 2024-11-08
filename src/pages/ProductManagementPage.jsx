import React, { useEffect, useState } from 'react';
import { getFabrics, addFabric, updateFabric, deleteFabric } from '../services/fabricService';
import ProductAdmin from '../components/ProductAdmin';
import AddProduct from '../components/AddProduct';
import './ProductManagementPage.css';

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
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = async (newFabric) => {
    setError(null);

    try {
      const addedFabric = await addFabric(newFabric);
      setFabrics([...fabrics, addedFabric]);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async (id) => {
    setError(null); 

    try {
      await deleteFabric(id);
      setFabrics(fabrics.filter((fabric) => fabric.id !== id));
    } catch (err) {
      setError(err.message);
    }
  };

  const handleUpdate = async (updatedFabric) => {
    setError(null); 
  
    try {
      const updated = await updateFabric(updatedFabric);
      setFabrics((prevFabrics) => 
        prevFabrics.map((fabric) => (fabric.id === updated.id ? updated : fabric))
      );
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="product-management-container">
      <h2>Fabric Management</h2>
      <button onClick={() => setIsAdding(true)} className="add-product-button">
        Add Fabric
      </button>

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