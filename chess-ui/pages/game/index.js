import React, { useState, useEffect } from "react";
import axios from "axios";

const Game = () => {
  const [games, setGames] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get("http://localhost:8080/game");
      console.log(result.data);
      setGames(result.data);
    };
    fetchData();
  }, []);

  return (
    <div>
      <h1>Game</h1>
      <ul>
        {games.map((game) => (
          <li key={game.gameID}>{JSON.stringify(game)}</li>
        ))}
      </ul>
    </div>
  );
};

export default Game;
