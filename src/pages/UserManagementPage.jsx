import React, { useEffect, useState } from "react";
import { getAllUsers, deleteUser, promoteToAdmin, fireEmployee } from "../services/userService";
import "./UserManagementPage.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { FaTrash, FaUserShield, FaUserSlash } from "react-icons/fa";
import TokenManager from '../services/tokenManager';

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await getAllUsers();
        setUsers(response.users);
      } catch (err) {
        setError("Failed to fetch users. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  const handleDelete = async (id) => {
    try {
      const token = TokenManager.getAccessToken();
      await deleteUser(id, token);
      setUsers(users.filter((user) => user.id !== id));
      alert("User deleted successfully.");
    } catch (err) {
      setError(err.message);
    }
  };

  const handlePromote = async (id) => {
    try {
      const result = await promoteToAdmin(id);
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === id ? { ...user, roles: [...user.roles, "ADMIN"] } : user
        )
      );
      alert(result.message);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleFire = async (id) => {
    try {
      const token = TokenManager.getAccessToken();
      const result = await fireEmployee(id, token); // Fire employee service
      setUsers(users.filter((user) => user.id !== id));
      alert(result.message || "Employee fired successfully.");
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading)
    return (
      <div className="loading">
        <FontAwesomeIcon icon={faSpinner} spinPulse size="2x" />
      </div>
    );

  if (error) return <div className="error">{error}</div>;

  return (
    <div className="user-management">
      <h1>User Management</h1>
      {error && <p className="error-message">{error}</p>}
      <table className="user-table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Username</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.username}</td>
              <td>{user.firstName}</td>
              <td>{user.lastName}</td>
              <td>{user.email}</td>
              <td>
                <div className="action-buttons">
                  <button
                    className="delete-btn"
                    onClick={() => handleDelete(user.id)}
                    title="Delete User"
                  >
                    <FaTrash />
                  </button>
                  <button
                    className="promote-btn"
                    onClick={() => handlePromote(user.id)}
                    title="Promote to Admin"
                  >
                    <FaUserShield />
                  </button>
                  <button
                    className="fire-btn"
                    onClick={() => handleFire(user.id)}
                    title="Fire Employee"
                  >
                    <FaUserSlash />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UserManagementPage;