import React, { useEffect, useState } from 'react';
import WebSocketService from '../services/webSocketService'; // The service we created
import Notification from './Notification'; // Import the new Notification component

const WebSocketNotification = () => {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    // Connect to WebSocket when the component is mounted
    WebSocketService.connect();

    // Clean up and disconnect from WebSocket when the component is unmounted
    return () => {
      WebSocketService.disconnect();
    };
  }, []);

  // Handle receiving notifications and updating state
  const handleNewNotification = (message) => {
    setNotifications((prevNotifications) => [...prevNotifications, message.text]);
  };

  const handleRemoveNotification = (index) => {
    setNotifications((prevNotifications) => prevNotifications.filter((_, i) => i !== index));
  };

  return (
    <div>
      {notifications.length > 0 &&
        notifications.map((msg, index) => (
          <Notification key={index} message={msg} onClose={() => handleRemoveNotification(index)} />
        ))
      }
    </div>
  );
};

export default WebSocketNotification;