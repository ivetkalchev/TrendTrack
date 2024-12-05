import React, { useEffect, useState } from "react";
import { getAllUsers, deleteUser, promoteToAdmin } from "../services/userService";
import "./UserManagementPage.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { FaEdit, FaTrash, FaUserShield } from "react-icons/fa";

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
      await deleteUser(id);
      setUsers(users.filter((user) => user.id !== id));
      alert("User deleted successfully.");
    } catch (err) {
      setError("Failed to delete user.");
    }
  };

  const handlePromote = async (id) => {
    try {
      await promoteToAdmin(id);
      setUsers(
        users.map((user) =>
          user.id === id ? { ...user, roles: [...user.roles, "ADMIN"] } : user
        )
      );
      alert("User promoted to admin.");
    } catch (err) {
      setError("Failed to promote user.");
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
                    className="edit-btn"
                    onClick={() => alert(`Edit functionality not implemented yet for user ID ${user.id}`)}
                    title="Edit User"
                  >
                    <FaEdit />
                  </button>
                  {!(user.roles || []).includes("ADMIN") && (
                    <button
                      className="promote-btn"
                      onClick={() => handlePromote(user.id)}
                      title="Promote to Admin"
                    >
                      <FaUserShield />
                    </button>
                  )}
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