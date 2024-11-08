import React, { useEffect, useState } from 'react';
import axios from 'axios';
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

  const fetchFabrics = () => {
    setLoading(true);
    setError(null);

    axios.get('http://localhost:8080/fabrics')
      .then((response) => {
        setFabrics(response.data.fabrics);
      })
      .catch((error) => {
        setError(error.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleAdd = (newFabric) => {
    setError(null); 

    axios
      .post('http://localhost:8080/fabrics', newFabric)
      .then((response) => {
        setFabrics([...fabrics, response.data]);
      })
      .catch((error) => {
        setError(`Failed to add fabric: ${error.message}`);
      });
  };

  const handleDelete = (id) => {
    setError(null);

    axios
      .delete(`http://localhost:8080/fabrics/${id}`)
      .then(() => {
        setFabrics(fabrics.filter((fabric) => fabric.id !== id));
      })
      .catch((error) => {
        setError(`Failed to delete fabric: ${error.message}`);
      });
  };

  const handleUpdate = (updatedFabric) => {
    setError(null);

    axios
      .put(`http://localhost:8080/fabrics/${updatedFabric.id}`, updatedFabric)
      .then(() => {
        setFabrics(
          fabrics.map((fabric) => (fabric.id === updatedFabric.id ? updatedFabric : fabric))
        );
      })
      .catch((error) => {
        setError(`Failed to update fabric: ${error.message}`);
      });
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

      {Array.isArray(fabrics) && fabrics.length === 0 ? (
        <p>No fabrics available.</p>
      ) : (
        <div className="product-list">
          {Array.isArray(fabrics) &&
            fabrics.map((fabric) => (
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