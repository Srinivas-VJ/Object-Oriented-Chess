import React, { useState } from "react";
import { useRouter } from "next/router";
import axios from "axios";
import { SERVER_ENDPOINT } from "../../config";


const GamePage = () => {
	const URL = SERVER_ENDPOINT +  "/game";
	const [player1, setPlayer1] = useState("");
	const [player2, setPlayer2] = useState("");
	const [gameId, setGameId] = useState("");
	const [currentPlayerColor, setCurrentPlayerColor] = useState("white");
	const router = useRouter();
      
	const handleStartGame = async () => {
		const response = await axios.post(URL, {
			"playerWhite": player1,
			"playerBlack": player2,
			"currentPlayerColor": "white",
			"moves": [],
			"status": "intialted",
			"description": "fresh game"
		    });
	  router.push({
	    pathname: `/newgame/${response.data}`,
	    query: { player1, player2, gameId, currentPlayerColor },
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
		  <label htmlFor="currentPlayerColor">Play as :</label>
			<select name="colors" id="color" onChange={(e) => setCurrentPlayerColor(e.target.value)}>
			<option value="white">WHITE</option>
			<option value="black">BLACK</option>
			</select>
	      </div>
	      <div className="form-group">
	      </div>
	      <button type="button" onClick={handleStartGame}>
		Start Game
	      </button>
	    </form>
	  </div>
	);
      };
      
export default GamePage;
