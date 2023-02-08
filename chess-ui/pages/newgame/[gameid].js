import React, { useState } from "react";
import { kingWhite, queenWhite, rookWhite, bishopWhite, knightWhite, pawnWhite, kingBlack, queenBlack, rookBlack, bishopBlack, knightBlack, pawnBlack } from "./pieces.js";

const ChessBoard = (player1, player2, gameId) => {
	const initialBoard = [
		["R", "N", "B", "Q", "K", "B", "N", "R"],
		["P", "P", "P", "P", "P", "P", "P", "P"],
		[" ", " ", " ", " ", " ", " ", " ", " "],
		[" ", " ", " ", " ", " ", " ", " ", " "],
		[" ", " ", " ", " ", " ", " ", " ", " "],
		[" ", " ", " ", " ", " ", " ", " ", " "],
		["p", "p", "p", "p", "p", "p", "p", "p"],
		["r", "n", "b", "q", "k", "b", "n", "r"]
	      ];
  const [squares, setSquares] = useState(initialBoard);

  const handleSquareClick = (i) => {
    const squaresCopy = squares.slice();
    squaresCopy[i] = "X";
    setSquares(squaresCopy);
  };

  const renderSquare = (i) => {
    let piece = null;

    switch (squares[i]) {
      case "K":
        piece = kingWhite;
        break;
      case "Q":
        piece = queenWhite;
        break;
      case "R":
        piece = rookWhite;
        break;
      case "B":
        piece = bishopWhite;
        break;
      case "N":
        piece = knightWhite;
        break;
      case "P":
        piece = pawnWhite;
        break;
      case "k":
        piece = kingBlack;
        break;
      case "q":
        piece = queenBlack;
        break;
      case "r":
        piece = rookBlack;
        break;
      case "b":
        piece = bishopBlack;
        break;
      case "n":
        piece = knightBlack;
        break;
      case "p":
        piece = pawnBlack;
        break;
      default:
        piece = null;
    }

    return (
      <button className="square" onClick={() => handleSquareClick(i)}>
        {piece && <img src={piece} alt={squares[i]} />}
      </button>
    );
  };

  return (
    <div>
      {[0, 1, 2, 3, 4, 5, 6, 7].map((row) => (
        <div className="board-row">
          {[0, 1, 2, 3, 4, 5, 6, 7].map((col) => renderSquare(row * 8 + col))}
        </div>
      ))}
    </div>
  );
};



      
      ChessBoard.getInitialProps = async ({ query }) => {
	return {
	  player1: query.player1,
	  player2: query.player2,
	  gameId: query.gameId,
	};
      };
      
      export default ChessBoard;