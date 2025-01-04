import React, { useEffect, useState } from "react";
import { getUserDetailsById, editUser } from "../services/userService";
import TokenManager from "../services/tokenManager";
import EditUser from "../components/EditUser";
import "./PersonalInfoPage.css";

const PersonalInfoPage = () => {
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const userId = TokenManager.getUserIdFromToken();
        if (!userId) throw new Error("Invalid or missing user ID in token.");
        const response = await getUserDetailsById(userId);
        setUser(response);
      } catch (err) {
        setError("Failed to load your personal information. Please try again.");
      }
    };

    fetchUserDetails();
  }, []);

  const onSave = async (updatedUser) => {
    try {
      await editUser(updatedUser.id, updatedUser, TokenManager.getAccessToken());
      setSuccessMessage("Your information has been updated successfully.");
      setError(null);
    } catch (err) {
      const errorResponse = err.response?.data;
      if (errorResponse?.violations) {
        setError(
          errorResponse.violations.map((v) => `${v.field}: ${v.message}`).join(", ")
        );
      } else {
        setError("Failed to update your information. Please try again later.");
      }
      setSuccessMessage(null);
    }
  };

  const onCancel = () => {
    setSuccessMessage(null);
    setError(null);
  };

  return (
    <div className="personal-info-page">
      <h1>Update Personal Information</h1>
      {error && <p className="error-message">{error}</p>}
      {successMessage && <p className="success-message">{successMessage}</p>}
      {user ? (
        <EditUser user={user} onSave={onSave} onCancel={onCancel} />
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default PersonalInfoPage;