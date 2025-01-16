import React, { useState, useEffect } from 'react';
import { FaTimes } from 'react-icons/fa';
import './Notification.css';

const Notification = ({ message, onClose }) => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      if (onClose) {
        onClose();
      }
    }, 13000); 

    return () => clearTimeout(timer);
  }, []);

  const handleClose = () => {
    setIsVisible(false);
    if (onClose) {
      onClose();
    }
  };

  return (
    isVisible && (
      <div className="notification">
        {message}
        <button onClick={handleClose}>
          <FaTimes />
        </button>
      </div>
    )
  );
};

export default Notification;