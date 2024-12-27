import React from "react";
import { useForm } from "react-hook-form";
import { login } from "../services/authService";
import TokenManager from "../services/tokenManager";

const Login = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();

  const onSubmit = async (data) => {
    try {
      const response = await login(data);
      TokenManager.setAccessToken(response.accessToken); // Save token and claims
      console.log("Login Successful:", response);
      alert("Login Successful!");
    } catch (error) {
      console.error("Login Failed:", error.message);
      alert("Login Failed. Please try again.");
    }
  };

  return (
    <form className="form" onSubmit={handleSubmit(onSubmit)}>
      <h2>Welcome Back</h2>
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
      <button type="submit" className="submit-btn">Login</button>
    </form>
  );
};

export default Login;