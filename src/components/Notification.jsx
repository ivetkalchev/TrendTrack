// src/components/Notification.js
import React, { useEffect, useState } from 'react';
import './Notification.css'; // Import CSS for styling

const Notification = ({ message, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 5000); // Auto-close notification after 5 seconds

    return () => clearTimeout(timer);
  }, [message, onClose]);

  return (
    <div className="notification-container">
      <div className="notification">
        <span>{message}</span>
        <button className="close-button" onClick={onClose}>×</button>
      </div>
    </div>
  );
};

export default Notification;