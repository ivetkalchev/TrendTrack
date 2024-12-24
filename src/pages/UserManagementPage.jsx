import React, { useEffect, useState } from "react";
import { getAllUsers, deleteUser, editUser } from "../services/userService";
import "./UserManagementPage.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { FaTrash, FaEdit } from "react-icons/fa";
import TokenManager from "../services/tokenManager";
import EditUser from "../components/EditUser";

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editingUser, setEditingUser] = useState(null);

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

  const handleSave = async (updatedUser) => {
    try {
      const token = TokenManager.getAccessToken();
      await editUser(updatedUser.id, updatedUser, token); // Pass updated user
      setUsers(
        users.map((user) => (user.id === updatedUser.id ? updatedUser : user))
      );
      setEditingUser(null);
      alert("User updated successfully.");
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
      {editingUser && (
        <EditUser
          user={editingUser}
          onSave={handleSave}
          onCancel={() => setEditingUser(null)}
        />
      )}
      <table className="user-table">
        <thead>
          <tr>
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
                    className="edit-btn"
                    onClick={() => setEditingUser(user)}
                    title="Edit User"
                  >
                    <FaEdit />
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