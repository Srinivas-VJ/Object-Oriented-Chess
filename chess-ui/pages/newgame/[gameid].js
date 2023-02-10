import { useState , useEffect} from "react";
import  {useRouter} from 'next/router';
import {Chess} from 'chess.js'
import { Chessboard } from "react-chessboard";
import { SERVER_ENDPOINT } from "../../config";


const chess = new Chess();
var stompClient = null;
export default function PlayGame(player1, player2, gameId, playerColor) {
  const [game, setGame] = useState(chess);
  const [turn, setTurn] = useState("white");
  const router = useRouter();
  gameId = router.query.gameid;
  playerColor = router.query.currentPlayerColor;

  useEffect(() => {
    var socket = new SockJS(SERVER_ENDPOINT + '/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        var path = '/topic/move/' + gameId
        stompClient.subscribe(path, function (greeting) {
            const move = JSON.parse(greeting.body);
            if (move.color == turn) {
               makeAMove({
                from: move.from,
                to: move.to,
                promotion: "q", // always promote to a queen for example simplicity
              });
              // onDrop(move.from, move.to);
              console.log("here")
            }
            console.log("Manual make move method is callled should be called:)")
            console.log(`got response----------------------------------- ${greeting.body}`)
        });
    });
  }, []);

  useEffect(() => {}, [game, turn]);


  function makeAMove(move) {
    console.log('call to make a move');
    const gameCopy = new Chess();
    gameCopy.loadPgn(game.pgn());
    const result = gameCopy.move(move);
    setGame(gameCopy);
    setTurn(turn == "white" ? "black" : "white");
    return result; // null if the move was illegal, the move object if the move was legal
  }

  function makeRandomMove() {
    const possibleMoves = game.moves();
    if (game.game_over() || game.in_draw() || possibleMoves.length === 0) return; // exit if the game is over
    const randomIndex = Math.floor(Math.random() * possibleMoves.length);
    makeAMove(possibleMoves[randomIndex]);
  }

  function onDrop(sourceSquare, targetSquare) {
    console.log("call to on drop " + sourceSquare + " "  + targetSquare)
    const move = makeAMove({
      from: sourceSquare,
      to: targetSquare,
      promotion: "q", // always promote to a queen for example simplicity
    });

    let path = "/app/move/" + gameId;
    stompClient.send(path, {}, JSON.stringify({'from': sourceSquare, 'to' : targetSquare, 'color': playerColor, 'fen' : game.fen()}));
    // illegal move
    if (move === null) return false;
    console.log(chess)
    return true;
  }

  return (<div style={{width:  "750px", border: "13px solid white", padding: "10px", alignContent: "center", justifyContent: "center", alignItems: "center", justifyItems: "center"}}>
    <span> Current player turn : {turn}</span>
    <Chessboard 
        alignContent = "center"
        position={game.fen()} 
        onPieceDrop={onDrop} 
        boardOrientation={"white"} 
        // customDarkSquareStyle={{ borderRadius: '5px', boxShadow: '0 5px 15px rgba(0, 0, 0, 0.5 '}}
        customPieces = {
          { bK: ({squareWidth="32px"}) => <div style={{fontSize: "10px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Chess_kdt45.svg/90px-Chess_kdt45.svg.png"></img></div> ,
            wP: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Chess_plt45.svg/90px-Chess_plt45.svg.png"></img></div>,
            bP: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Chess_pdt45.svg/90px-Chess_pdt45.svg.png"></img></div>,
            wN: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Chess_nlt45.svg/90px-Chess_nlt45.svg.png"></img></div>,
            bN: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Chess_ndt45.svg/90px-Chess_ndt45.svg.png"></img></div> ,
            bR: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Chess_rdt45.svg/90px-Chess_rdt45.svg.png"></img></div> ,
            wR: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Chess_rlt45.svg/90px-Chess_rlt45.svg.png"></img></div> ,
            bB: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Chess_bdt45.svg/90px-Chess_bdt45.svg.png"></img></div> ,
            wB: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Chess_blt45.svg/90px-Chess_blt45.svg.png"></img></div> ,
            wQ: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Chess_qlt45.svg/90px-Chess_qlt45.svg.png"></img></div> ,
            bQ: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Chess_qdt45.svg/90px-Chess_qdt45.svg.png"></img></div> ,
            wK: ({squareWidth="32px"}) => <div style={{fontSize: "90px"}}><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Chess_klt45.svg/90px-Chess_klt45.svg.png"></img></div> ,
        }
        }
        />

  </div>)
  
}
PlayGame.getInitialProps = async ({ query }) => {
  const player1 = query.player1;
  const player2 = query.player2;
  const gameid = query.gameid;
  const playerColor = query.currentPlayerColor;
  return { player1, player2, gameid, playerColor };
};


