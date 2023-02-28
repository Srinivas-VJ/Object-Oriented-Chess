import React, { useState, useEffect } from "react";
import axios from "axios";
import { useRouter } from "next/router";



const User = () => {
  const [user, setUser] = useState([]);
  const router = useRouter();
  useEffect(() => {
    if (!router.isReady)
            return;
    const fetchData = async () => {
        console.log(router.query);
        const result = await axios.get("/api/users/" + router.query.username );
        console.log(result.data);
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
