import React, { useState } from "react";
import { useRouter } from "next/router";
import axios from "axios";


const GamePage = () => {
	const URL = "http://localhost:8080/game"
	const [player1, setPlayer1] = useState("");
	const [player2, setPlayer2] = useState("");
	const [gameId, setGameId] = useState("");
	const router = useRouter();
      
	const handleStartGame =  () => {
		const response = axios.post(URL, {
			"gameID": gameId,
			"playerWhite": player1,
			"playerBlack": player2,
			"currentPlayerColor": "white",
			"moves": [],
			"status": "intialted",
			"description": "fresh game"
		    });
	  router.push({
	    pathname: `/newgame/${gameId}`,
	    query: { player1, player2, gameId },
	  });
	};
      
	return (
	  <div className="container">
	    <h1 className="title">New Game</h1>
	    <form className="form">
	      <div className="form-group">
		<label htmlFor="player1">Player 1:</label>
		<input
		  type="text"
		  id="player1"
		  value={player1}
		  onChange={(e) => setPlayer1(e.target.value)}
		/>
	      </div>
	      <div className="form-group">
		<label htmlFor="player2">Player 2:</label>
		<input
		  type="text"
		  id="player2"
		  value={player2}
		  onChange={(e) => setPlayer2(e.target.value)}
		/>
	      </div>
	      <div className="form-group">
		<label htmlFor="gameId">Game ID:</label>
		<input
		  type="text"
		  id="gameId"
		  value={gameId}
		  onChange={(e) => setGameId(e.target.value)}
		/>
	      </div>
	      <button type="button" onClick={handleStartGame}>
		Start Game
	      </button>
	    </form>
	  </div>
	);
      };
      
export default GamePage;
