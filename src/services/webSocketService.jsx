import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import TokenManager from '../services/tokenManager'; // Assuming you're using the TokenManager

class WebSocketService {
  constructor() {
    this.stompClient = null;
    this.userId = TokenManager.getUserIdFromToken();
    this.token = TokenManager.getAccessToken();
  }

  connect() {
    if (!this.userId || !this.token) {
      console.error('User is not authenticated');
      return;
    }

    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${this.token}`,
      },
      onConnect: (frame) => {
        console.log('Connected: ' + frame);
        // After connecting, you can subscribe to specific channels based on the user
        this.subscribeToNotifications();
      },
      onStompError: (error) => {
        console.error('WebSocket Error:', error);
        // Optionally, handle reconnection logic here
      },
      reconnectDelay: 5000,  // Retry connection every 5 seconds if disconnected
    });

    this.stompClient.activate();
  }

  subscribeToNotifications() {
    if (!this.stompClient) return;

    // Subscribe to notifications for the specific user
    this.stompClient.subscribe(`/user/${this.userId}/queue/notifications`, (messageOutput) => {
      const message = JSON.parse(messageOutput.body);
      console.log('New Notification:', message.text);
      // You can handle the received message here, e.g., by updating the UI
    });
  }

  disconnect() {
    if (this.stompClient && this.stompClient.active) {
      this.stompClient.deactivate();
      console.log('Disconnected from WebSocket');
    }
  }
}

export default new WebSocketService();