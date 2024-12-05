import React, { useEffect, useState } from 'react';
import { getFabrics, addFabric, updateFabric, deleteFabric } from '../services/fabricService';
import ProductAdmin from '../components/FabricControlPanel';
import AddProduct from '../components/AddFabric';
import './FabricManagementPage.css';
import { FaPlus } from 'react-icons/fa';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';

const ProductManagementPage = () => {
  const [fabrics, setFabrics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [feedbackMessage, setFeedbackMessage] = useState('');
  const [isAdding, setIsAdding] = useState(false);
  const [filterQuery, setFilterQuery] = useState('');

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
      setFeedbackMessage('Fabric added successfully!');
      setTimeout(() => setFeedbackMessage(''), 3000);
    } catch (err) {
      setError('Failed to add fabric. Please try again.');
    }
  };

  const handleDelete = async (id) => {
    setError(null);
    try {
      await deleteFabric(id);
      setFabrics((prevFabrics) => prevFabrics.filter((fabric) => fabric.id !== id));
      setFeedbackMessage('Fabric deleted successfully!');
      setTimeout(() => setFeedbackMessage(''), 3000);
    } catch (err) {
      setError('Failed to delete fabric. Please try again.');
    }
  };

  const handleUpdate = async (updatedFabric) => {
    setError(null);
    try {
      await updateFabric(updatedFabric);
      fetchFabrics();
      setFeedbackMessage('Fabric updated successfully!');
      setTimeout(() => setFeedbackMessage(''), 3000);
    } catch (err) {
      setError('Failed to update fabric. Please try again.');
    }
  };

  const filteredFabrics = fabrics.filter((fabric) =>
    ['name', 'material', 'color'].some((key) =>
      fabric[key]?.toLowerCase().includes(filterQuery.toLowerCase())
    )
  );

  if (loading)
    return (
      <div className="loading">
        <FontAwesomeIcon icon={faSpinner} spinPulse />
      </div>
    );

  if (error) return <div className="error">{error}</div>;

  return (
    <div className="product-management-container">
      <div className="header-container">
        <h2>Fabric Management</h2>
        <div className="actions">
          <input
            type="text"
            placeholder="Search fabrics..."
            value={filterQuery}
            onChange={(e) => setFilterQuery(e.target.value)}
            className="filter-input"
          />
          <button onClick={() => setIsAdding(true)} className="add-product-button">
            <FaPlus />
          </button>
        </div>
      </div>

      {feedbackMessage && <div className="feedback-message">{feedbackMessage}</div>}

      {isAdding && (
        <AddProduct
          onAdd={handleAdd}
          onClose={() => setIsAdding(false)}
        />
      )}

      {filteredFabrics.length === 0 ? (
        <p id="no-result">No results match your search.</p>
      ) : (
        <div className="product-list">
          {filteredFabrics.map((fabric) => (
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