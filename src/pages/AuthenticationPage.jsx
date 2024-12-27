import React, { useState } from "react";
import Login from "../components/Login";
import Register from "../components/Register";
import "./AuthenticationPage.css";

const AuthenticationPage = () => {
  const [isLogin, setIsLogin] = useState(true);

  const toggleAuthMode = () => setIsLogin(!isLogin);

  return (
    <div className="authentication-page">
      <div className="auth-container">
        {isLogin ? (
          <>
            <Login />
            <p>or</p>
            <span className="toggle-link" onClick={toggleAuthMode}>
              Register
            </span>
          </>
        ) : (
          <>
            <Register />
            <p>or</p>
            <span className="toggle-link" onClick={toggleAuthMode}>
              Login
            </span>
          </>
        )}
      </div>
    </div>
  );
};

export default AuthenticationPage;