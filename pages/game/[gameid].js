import React, { useState, useEffect } from "react";
import axios from "axios";
import { useRouter } from "next/router";



const Game = () => {
  const [game, setGameß] = useState([]);
  const router = useRouter();
  useEffect(() => {
    if (!router.isReady)
            return;
    const fetchData = async () => {
        console.log(router.query);
        const result = await axios.get("/api/game/" + router.query.gameid );
        console.log(result.data);
     setGameß(result.data);
    
    };
    fetchData();
  }, [router.isReady]);

  return (
    <div>
      <h1>Game</h1>
      <pre>{JSON.stringify(game, null, 4)}</pre>
    </div>
  );
};

export default Game;
