import React, { useState } from "react";
import axios from "axios";
import { Form, Input, Button } from "antd";
import { useRouter } from "next/router";
import { setAuthToken, setUserDetails } from "../utils/authenticate";
import Link from "next/link";
import { SERVER_ENDPOINT } from "../config";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const handleLogin = async () => {
    try {
      const response = await axios.post(
        SERVER_ENDPOINT + "/auth/authenticate",
        {
          email,
          password,
        }
        // { withCredentials: true }
      );
      console.log(response.data);
      console.log(response);
      const token = response.data.token;
      const user = response.data.userResponse;
      setAuthToken(token);
      setUserDetails(user);
      router.push("/");
    } catch (error) {
      console.error(error);
    }
  };

  const handleEmailChange = (e) => setEmail(e.target.value);
  const handlePasswordChange = (e) => setPassword(e.target.value);

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "40vh",
      }}
    >
      <div
        style={{
          width: 400,
          padding: 35,
          border: "1px solid #ccc",
          borderRadius: 4,
        }}
      >
        <h1>Login</h1>
        <br></br>
        <Form onFinish={handleLogin}>
          <Form.Item
            name="email"
            label="Email"
            rules={[{ required: true, message: "Please input your email!" }]}
          >
            <Input onChange={handleEmailChange} />
          </Form.Item>

          <Form.Item
            name="password"
            label="Password"
            rules={[{ required: true, message: "Please input your password!" }]}
          >
            <Input.Password onChange={handlePasswordChange} />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit">
              Log in
            </Button>
          </Form.Item>
        </Form>

        <div>
          Don't have an account? <Link href={"/register"}>Register here</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
