import React from "react";
import { useForm } from "react-hook-form";
import { register as registerUser } from "../services/authService";

const Register = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();

  const onSubmit = async (data) => {
    try {
      const response = await registerUser(data);
      console.log("Registration Successful:", response);
      alert("Registration Successful!");
    } catch (error) {
      console.error("Registration Failed:", error.message);
      alert("Registration Failed. Please try again.");
    }
  };

  return (
    <form className="form" onSubmit={handleSubmit(onSubmit)}>
      <h2>Join Our Family</h2>
      <div className="form-control">
        <label>Username</label>
        {errors.username && <span className="error-message">{errors.username.message}</span>}
        <input
          {...register("username", { required: "Username is required" })}
          placeholder="Enter your username"
        />
      </div>
      <div className="form-control">
        <label>Password</label>
        {errors.password && <span className="error-message">{errors.password.message}</span>}
        <input
          type="password"
          {...register("password", {
            required: "Password is required",
            minLength: {
              value: 8,
              message: "Password must be at least 8 characters long",
            },
          })}
          placeholder="Enter your password"
        />
      </div>
      <div className="form-control">
        <label>Email</label>
        {errors.email && <span className="error-message">{errors.email.message}</span>}
        <input
          {...register("email", {
            required: "Email is required",
            pattern: {
              value: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
              message: "Invalid email format",
            },
          })}
          placeholder="Enter your email"
        />
      </div>
      <div className="form-control">
        <label>First Name</label>
        {errors.firstName && <span className="error-message">{errors.firstName.message}</span>}
        <input
          {...register("firstName", { required: "First name is required" })}
          placeholder="Enter your first name"
        />
      </div>
      <div className="form-control">
        <label>Last Name</label>
        {errors.lastName && <span className="error-message">{errors.lastName.message}</span>}
        <input
          {...register("lastName", { required: "Last name is required" })}
          placeholder="Enter your last name"
        />
      </div>
      <button type="submit" className="submit-btn">Register</button>
    </form>
  );
};

export default Register;