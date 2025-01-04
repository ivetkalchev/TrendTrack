import React, { useState, useEffect } from "react";
import { getFabrics } from "../services/fabricService";
import PurchaseFabric from "../components/PurchaseFabric";
import FilterFabrics from "../components/FilterFabrics";
import "./CataloguePage.css";

const CataloguePage = () => {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [pagination, setPagination] = useState({ page: 0, size: 9 });
  const [filters, setFilters] = useState({
    name: "",
    material: "",
    color: "",
    minPrice: "",
    maxPrice: ""
  });

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getFabrics({ ...filters, page: pagination.page, size: pagination.size });
        setProducts(data.fabrics);
      } catch (error) {
        setError(error.message);
      }
    };
    fetchProducts();
  }, [filters, pagination]);

  const handleFilter = (newFilters) => {
    setFilters(newFilters);
    setPagination((prevState) => ({
      ...prevState,
      page: 0
    }));
  };

  const handlePagination = (newPage) => {
    setPagination((prevState) => ({
      ...prevState,
      page: newPage
    }));
  };


  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
    <div className="catalogue-page">
      <h1>Catalogue</h1>

      <FilterFabrics onFilter={handleFilter} />

      <div className="product-list">
        {products.length === 0 ? (
          <p>No products found.</p>
        ) : (
          products.map((product) => (
            <PurchaseFabric key={product.id} product={product} />
          ))
        )}
      </div>

      <div className="pagination-controls">
        <button
          onClick={() => handlePagination(pagination.page - 1)}
          disabled={pagination.page === 0}
        >
          Previous
        </button>
        <span>
          Page {pagination.page + 1}
        </span>
        <button
          onClick={() => handlePagination(pagination.page + 1)}
          disabled={!hasNextPage}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default CataloguePage;