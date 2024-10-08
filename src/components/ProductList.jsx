import React, { useEffect, useState } from "react";
import axios from "axios";
import "./ProductList.css"; 

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
          const response = await fetch('http://localhost:8080/products');
          if (!response.ok) {
              throw new Error(`Error: ${response.status} ${response.statusText}`);
          }
          const data = await response.json();

          setProducts(data.products || data); 
      } catch (error) {
          setError(error.message);
      } finally {
          setLoading(false);
      }
    };  

    fetchProducts();
  }, []);

  return (
    <div>
        <h2>Product List</h2>
        <ul>
            {products.map((product) => (
                <li key={product.idProduct}>
                    <h3>{product.name}</h3>
                    <p>{product.description}</p>
                    <p>Quantity: {product.quantity}</p>
                    <p>Price: €{product.price.toFixed(2)}</p>
                </li>
            ))}
        </ul>
    </div>
);
};

export default ProductList;