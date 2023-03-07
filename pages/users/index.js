import React, { useState, useEffect } from "react";
import axios from "axios";
import { getAuthToken } from "../../utils/authenticate";
import { SERVER_ENDPOINT } from "../../config";

const Users = () => {
  const [users, setUsers] = useState([]);
 

  useEffect(() => {
    const token = getAuthToken();
    console.log(token);
    var headers;
    if (token != null)
        headers = { Authorization: `Bearer ${token}` };
    const fetchData = async () => {
      const result = await axios.get(SERVER_ENDPOINT + "/users", {headers});
      console.log(result.data);
      setUsers(result.data);
    };
    fetchData();
  }, []);

  useEffect( () => {

  }, [users]);

  return (
    <div>
      <h1>Users</h1>
      <p>{JSON.stringify(users)}</p>
    </div>
  );
};

export default Users;
