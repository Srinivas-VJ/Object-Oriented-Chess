import React, { useState } from "react";
import axios from "axios";
import { useRouter } from "next/router";

function Register() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [userEmail, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const handleRegister = async () => {
    if (!username || !userEmail || !password || !confirmPassword) {
      setErrorMessage("Please fill all fields");
      return;
    }

    if (password !== confirmPassword) {
      setErrorMessage("Passwords do not match");
      return;
    }

    try {
      const response = await axios.post("/api/register", {
        username,
        userEmail: userEmail,
        password,
        profilePicture : ""
      });
      console.log(response.data)
      setSuccessMessage(response.data.message);
      setUsername("");
      setEmail("");
      setPassword("");
      setConfirmPassword("");
      router.push("/login");
    } catch (error) {
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage("Something went wrong");
      }
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "40vh" }}>
      <div style={{ width: "400px", padding: "40px", border: "1px solid #ccc", borderRadius: "5px" }}>
        <h1 style={{ textAlign: "center" }}>Register</h1>
        <div>
          {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
          {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
          <div style={{ margin: "10px 0" }}>
            <label htmlFor="username">Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={{ marginLeft: "10px" }}
            />
          </div>
          <div style={{ margin: "10px 0" }}>
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              value={userEmail}
              onChange={(e) => setEmail(e.target.value)}
              style={{ marginLeft: "32px" }}
            />
          </div>
          <div style={{ margin: "10px 0" }}>
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{ marginLeft: "16px" }}
            />
          </div>
          <div style={{ margin: "10px 0" }}>
            <label htmlFor="confirmPassword">Confirm Password:</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              style={{ marginLeft: "16px" }}
            />
          </div>
          <button onClick={handleRegister} style={{ marginTop: "20px", width: "100%" }}>Register</button>
        </div>
      </div>
    </div>
    )
  }
 
export default Register;
