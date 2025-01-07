import React, { useState } from 'react';
import { FaTimes } from 'react-icons/fa';
import './Notification.css';

const Notification = ({ message }) => {
  const [isVisible, setIsVisible] = useState(true);

  const handleClose = () => {
    setIsVisible(false);
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