import React, { useState, useEffect } from "react";
import axios from "axios";

const Users = () => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get("/api/users");
      console.log(result.data);
      setUsers(result.data);
    };
    fetchData();
  }, []);

  return (
    <div>
      <h1>Users</h1>
      <ul>
        {users.map((user) => (
          <li key={user.userName}>{user.userName}</li>
        ))}
      </ul>
    </div>
  );
};

export default Users;
