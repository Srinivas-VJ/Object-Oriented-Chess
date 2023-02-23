import {
	useState,
	useEffect
} from "react";
import {
	useRouter
} from 'next/router';
import {
	Modal,
	Button
} from "antd";
import {
	Chess
} from 'chess.js'
import {
	Chessboard
} from "react-chessboard";
import {
	SERVER_ENDPOINT
} from "../../config";
import axios from "axios";


const chess = new Chess();
var stompClient = null;
var called = false;

export default function PlayGame(player1, player2, gameId, playerColor) {
	const [game, setGame] = useState(chess);
	const [turn, setTurn] = useState("white");
	const [visible, setVisible] = useState(false);
	const router = useRouter();
	const handleOk = () => {
		setVisible(false);
		router.push("/game");
	};

	gameId = router.query.gameid;
	playerColor = router.query.currentPlayerColor;

	useEffect(() => {
		if (called) return;
		called = true;
		var socket = new SockJS(SERVER_ENDPOINT + "/gs-guide-websocket");
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log("Connected: " + frame);
			var path = "/topic/move/" + gameId;
			stompClient.subscribe(path, function(greeting) {
				const move = JSON.parse(greeting.body);
				console.log(
					`move from queue color - ${move.color}  and player is ${playerColor}`
				);
				if (move.color != playerColor) {
					makeAMove({
							from: move.from,
							to: move.to,
							promotion: "q", // always promote to a queen for example simplicity
						},
						move.fen
					);
				}
				if ((move.status != "active"))
					setVisible(true);
			});
		});
		// get fen from backend
    axios.get(SERVER_ENDPOINT + "/game/" + gameId + "/getFen")
    .then( res => {
      console.log("Fetching fen from backend");
      const  gameCopy = new Chess();
	  console.log(res);
	  console.log(res.data);
      gameCopy.load(res.data);
      setGame(gameCopy);
      if (res.data.split()[1] == "w" ? setTurn("white") : setTurn("black"));
      console.log(res);
    })

	}, []);

	useEffect(() => {}, [turn]);

	function makeAMove(move, fen) {
		// try {
		const gameCopy = new Chess();
		gameCopy.load(fen);
		const result = gameCopy.move(move);
		setGame(gameCopy);
		const tempTurn = fen.split(" ")[1];
		if (result != null) {

			setTurn(tempTurn == "w" ? "black" : "white");
			let path = "/app/move/" + gameId;
			axios.put(SERVER_ENDPOINT + "/move/" + gameId, {
						"from": move.from,
						"to": move.to,
						"color": playerColor,
						"fen": game.fen(),
					},
					// config
				)
				.then(res => {
          console.log(res)
          axios.put(SERVER_ENDPOINT + "/game/" + gameId + "/moved", {
						"from": move.from,
						"to": move.to,
						"color": playerColor,
						"fen": gameCopy.fen(),
					},
					// config
				)
				.then(res => console.log(res))
				.catch(err => console.log(err));
        })
				.catch(err => console.log(err));
		}
		return result; // null if the move was illegal, the move object if the move was legal
		// }
		// catch {
		//   console.log("INVALID MOVE");
		// }

	}

	function makeRandomMove() {
		const possibleMoves = game.moves();
		if (game.isGameOver() || game.isDraw() || possibleMoves.length === 0) return; // exit if the game is over
		const randomIndex = Math.floor(Math.random() * possibleMoves.length);
		makeAMove(possibleMoves[randomIndex], game.fen());
		var path = '/topic/move/' + gameId

		// stompClient.send(path, {}, JSON.stringify({'from': possibleMoves[randomIndex].from, 'to' : possibleMoves[randomIndex].from , 'color': playerColor == "white" ? "black" : "white" , 'fen' : game.fen()}));
	}

	function onDrop(sourceSquare, targetSquare) {

		if (game.get(sourceSquare).color != playerColor[0])
			return false;
		console.log("call to on drop " + sourceSquare + " " + targetSquare)
		const move = makeAMove({
			from: sourceSquare,
			to: targetSquare,
			promotion: "q", // always promote to a queen for example simplicity
		}, game.fen());

		// stompClient.send(
		//   path,
		//   {},
		//   JSON.stringify({
		//     from: sourceSquare,
		//     to: targetSquare,
		//     color: playerColor,
		//     fen: game.fen(),
		//   })
		// );
		// illegal move
		if (move === null) return false;
		return true;
	}

	return  <div style = {{
				width: "700px"
			}} >
		<span> Current player turn: {
			turn
		} </span>

		<
		Chessboard alignContent = "center"
		position = {
			game.fen()
		}
		onPieceDrop = {
			onDrop
		}
		boardOrientation = {
			playerColor
		}
		// customDarkSquareStyle={{ borderRadius: '5px', boxShadow: '0 5px 15px rgba(0, 0, 0, 0.5 '}}
		customPieces = {
			{
				bK: ({
					squareWidth = "32px"
				}) => <div style = {
					{
						fontSize: "10px"
					}
				}> <img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Chess_kdt45.svg/90px-Chess_kdt45.svg.png" /></div > ,
				wP: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > <img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Chess_plt45.svg/90px-Chess_plt45.svg.png" /> </div > ,
				bP: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > <img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Chess_pdt45.svg/90px-Chess_pdt45.svg.png" /></div > ,
				wN: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Chess_nlt45.svg/90px-Chess_nlt45.svg.png" /></div > ,
				bN: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Chess_ndt45.svg/90px-Chess_ndt45.svg.png" /> </div > ,
				bR: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Chess_rdt45.svg/90px-Chess_rdt45.svg.png" /></div > ,
				wR: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Chess_rlt45.svg/90px-Chess_rlt45.svg.png" /> </div > ,
				bB: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Chess_bdt45.svg/90px-Chess_bdt45.svg.png" /> </div > ,
				wB: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Chess_blt45.svg/90px-Chess_blt45.svg.png" /> </div > ,
				wQ: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Chess_qlt45.svg/90px-Chess_qlt45.svg.png" /> </div > ,
				bQ: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Chess_qdt45.svg/90px-Chess_qdt45.svg.png" /> </div > ,
				wK: ({
					squareWidth = "32px"
				}) => < div style = {
					{
						fontSize: "90px"
					}
				} > < img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Chess_klt45.svg/90px-Chess_klt45.svg.png" /> </div > ,
			}
		}
		/> <
		Modal title = "Game Over"
		open = {
			visible
		}
		onOk = {
			handleOk
		}
		footer = {
			[ <Button key = "submit"
				type = "primary"
				onClick = {
					handleOk
				} >
				OK </Button>
			]
		} >
		Game over!
		</Modal>

		</div> 

	}
	PlayGame.getInitialProps = async ({
		query
	}) => {
		const player1 = query.player1;
		const player2 = query.player2;
		const gameid = query.gameid;
		const playerColor = query.currentPlayerColor;
		return {
			player1,
			player2,
			gameid,
			playerColor
		};
	};