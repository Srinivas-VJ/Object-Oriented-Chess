import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import axios from "axios";
import { SERVER_ENDPOINT } from "../../config";
import { getAuthToken, getUserDetails } from "../../utils/authenticate";

const GamePage = () => {
  const URL = SERVER_ENDPOINT + "/game";
  const [player1, setPlayer1] = useState("");
  const [player2, setPlayer2] = useState("");
  const [gameId, setGameId] = useState("");
  const [currentPlayerColor, setCurrentPlayerColor] = useState("white");
  const router = useRouter();
  var token;

  useEffect(() => {
    token = getAuthToken();
    if (token != null) {
		console.log(token)
      const user = getUserDetails();
      setPlayer1(user.username);
    } else router.push("/login");
  }, []);

  const handleStartGame = async () => {
	const headers = { Authorization: `Bearer ${token}` };
	console.log(headers)
    const response = await axios.post(URL, {
      playerWhite: player1,
      playerBlack: player2,
      currentPlayerColor: "white",
      moves: [],
      status: "intialted",
      description: "fresh game",
    },
	{ headers });
    router.push({
      pathname: `/newgame/${response.data}`,
      query: { player1, player2, gameId, currentPlayerColor },
    });
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "40vh",
        backgroundColor: "#f5f5f5",
      }}
    >
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          backgroundColor: "#ffffff",
          padding: "2rem",
          borderRadius: "0.5rem",
          boxShadow: "0px 2px 6px rgba(0, 0, 0, 0.3)",
        }}
      >
        <h1 style={{ fontSize: "2rem", marginBottom: "2rem" }}>New Game</h1>
        <form className="form">
          <div className="form-group">
            <label htmlFor="player2">
              Enter user's username to challenge :{" "}
            </label>
            <input
              type="text"
              id="player2"
              value={player2}
              onChange={(e) => setPlayer2(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label htmlFor="currentPlayerColor">Play as :</label>
            <select
              name="colors"
              id="color"
              onChange={(e) => setCurrentPlayerColor(e.target.value)}
            >
              <option value="white">WHITE</option>
              <option value="black">BLACK</option>
            </select>
          </div>
          <div className="form-group"></div>
          <button type="button" onClick={handleStartGame}>
            Start Game
          </button>
        </form>
      </div>
    </div>
  );
};

export default GamePage;
