import React, { useState, useEffect } from 'react';
import { getFabrics, addFabric, updateFabric, deleteFabric } from '../services/fabricService';
import FabricControlPanel from '../components/FabricControlPanel';
import AddFabric from '../components/AddFabric';
import FilterFabrics from '../components/FilterFabrics';
import './FabricManagementPage.css';
import { FaPlus } from 'react-icons/fa';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';
import TokenManager from '../services/tokenManager';

const FabricManagementPage = () => {
  const [fabrics, setFabrics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [feedbackMessage, setFeedbackMessage] = useState('');
  const [isAdding, setIsAdding] = useState(false);
  const [pagination, setPagination] = useState({ page: 0, size: 9 });

  useEffect(() => {
    fetchFabrics(pagination);
  }, [pagination]);

  const fetchFabrics = async ({ page, size, filters }) => {
    setLoading(true);
    setError(null);

    try {
      const data = await getFabrics({ ...filters, page, size });
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
      const token = TokenManager.getAccessToken();
      await deleteFabric(id, token);
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
      setFabrics((prevFabrics) =>
        prevFabrics.map((fabric) =>
          fabric.id === updatedFabric.id ? updatedFabric : fabric
        )
      );
      setFeedbackMessage('Fabric updated successfully!');
      setTimeout(() => setFeedbackMessage(''), 3000);
    } catch (error) {
      setError('Failed to update fabric. Please try again.');
    }
  };

  const handleFilter = (filters) => {
    setPagination((prevState) => ({
      ...prevState,
      filters,
    }));
    fetchFabrics({ page: pagination.page, size: pagination.size, filters });
  };

  const handlePageChange = (newPage) => {
    setPagination((prevState) => ({
      ...prevState,
      page: newPage,
    }));
  };

  const hasNextPage = fabrics.length === pagination.size;

  if (loading)
    return (
      <div className="loading">
        <FontAwesomeIcon icon={faSpinner} spinPulse />
      </div>
    );

  if (error) return <div className="error">{error}</div>;

  return (
    <div className="product-management-container">
      <h2>Fabric Management</h2>
      <div className="header-container">
        <div className="actions">
          <FilterFabrics onFilter={handleFilter} />
          <button onClick={() => setIsAdding(true)} className="add-product-button">
            <FaPlus /> Add Product
          </button>
        </div>
      </div>

      {feedbackMessage && <div className="feedback-message">{feedbackMessage}</div>}

      {isAdding && <AddFabric onAdd={handleAdd} onClose={() => setIsAdding(false)} />}

      {fabrics.length === 0 ? (
        <p id="no-result">No results match your search.</p>
      ) : (
        <div className="product-list">
          {fabrics.map((fabric) => (
            <FabricControlPanel
              key={fabric.id}
              product={fabric}
              onDelete={handleDelete}
              onUpdate={handleUpdate}
            />
          ))}
        </div>
      )}

      <div className="pagination-controls">
        <button
          onClick={() => handlePageChange(pagination.page - 1)}
          disabled={pagination.page === 0}
        >
          Previous
        </button>
        <span>
          Page {pagination.page + 1}
        </span>
        <button
          onClick={() => handlePageChange(pagination.page + 1)}
          disabled={!hasNextPage}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default FabricManagementPage;