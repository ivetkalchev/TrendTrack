import React from "react";
import { useForm } from "react-hook-form";
import TokenManager from "../services/tokenManager";
import "./EditUser.css";

const EditUser = ({ user, onSave, onCancel }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      username: user.username,
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
    },
  });

  const onSubmit = (data) => {
    onSave({ ...data, id: user.id });
  };

  //admin role
  const claims = TokenManager.getClaims();
  const isAdmin = claims && claims.roles && claims.roles.includes("ADMIN");

  return (
    <div className="edit-user-form">
      <h3>Edit User</h3>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          <label>Username</label>
          <input
            type="text"
            className={errors.username ? "input-error" : ""}
            {...register("username", { required: "Username is required" })}
          />
          {errors.username && (
            <p className="error-row">{errors.username.message}</p>
          )}
        </div>
        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            className={errors.email ? "input-error" : ""}
            {...register("email", {
              required: "Email is required",
              pattern: {
                value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                message: "Invalid email address",
              },
            })}
          />
          {errors.email && <p className="error-row">{errors.email.message}</p>}
        </div>
        <div className="form-group">
          <label>First Name</label>
          <input
            type="text"
            className={errors.firstName ? "input-error" : ""}
            {...register("firstName", { required: "First name is required" })}
          />
          {errors.firstName && (
            <p className="error-row">{errors.firstName.message}</p>
          )}
        </div>
        <div className="form-group">
          <label>Last Name</label>
          <input
            type="text"
            className={errors.lastName ? "input-error" : ""}
            {...register("lastName", { required: "Last name is required" })}
          />
          {errors.lastName && (
            <p className="error-row">{errors.lastName.message}</p>
          )}
        </div>
        <div className="button-row">
          <button type="submit" className="save-btn">
            Save
          </button>
          {isAdmin && (
            <button type="button" className="cancel-btn" onClick={onCancel}>
              Cancel
            </button>
          )}
        </div>
      </form>
    </div>
  );
};

export default EditUser;