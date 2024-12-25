import React, { useState, useEffect } from "react";
import { getFabrics } from "../services/fabricService";
import PurchaseFabric from "../components/PurchaseFabric";
import "./CataloguePage.css";

const CataloguePage = () => {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
        try {
          const data = await getFabrics();
          console.log("Fetched products:", data);
          setProducts(data.fabrics);
        } catch (error) {
          console.error("Error fetching products:", error);
          setError(error.message);
        }
      };
    fetchProducts();
  }, []);  

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
    <div className="catalogue-page">
      <h1>Catalogue</h1>
      <div className="product-list">
        {products.map((product) => (
          <PurchaseFabric key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

export default CataloguePage;