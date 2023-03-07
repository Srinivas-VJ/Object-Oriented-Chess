import React, { useState, useEffect } from "react";
import axios from "axios";
import { useRouter } from "next/router";
import { SERVER_ENDPOINT } from "../../config";
import { getAuthToken } from "../../utils/authenticate";



const User = () => {
  const [user, setUser] = useState([]);
  const router = useRouter();
  var token = getAuthToken();
  var headers;
  if (token != null)
    headers = { Authorization: `Bearer ${token}` };
  
  useEffect(() => {
    if (!router.isReady)
            return;
    const fetchData = async () => {
        console.log(router.query);
        const result = await axios.get(SERVER_ENDPOINT + "/users/" + router.query.username  ,{headers});
     setUser(result.data);
    
    };
    fetchData();
  }, [router.isReady]);

  return (
    <div>
      <h1>User
    
      </h1>
      <ul>
          <li key={user.userName}>{JSON.stringify(user)}</li>
      </ul>
    </div>
  );
};

export default User;
