import { useState, useEffect } from "react";
import { useRouter } from "next/router";
import { Modal, Button } from "antd";
import { Chess } from "chess.js";
import { Chessboard } from "react-chessboard";
import { SERVER_ENDPOINT } from "../../config";
import axios from "axios";
import { getAuthToken } from "../../utils/authenticate";

const chess = new Chess();
var stompClient = null;
var called = false;
var token;
var drawOffered = "null"

export default function PlayGame(player1, player2, gameId, playerColor) {
  const [game, setGame] = useState(chess);
  const [turn, setTurn] = useState("white");
  const [visible, setVisible] = useState(false);
  const [gameStatus, setGameStatus] = useState("Game Over!");
  const [headers, setHeaders] = useState({})
  const [drawOfferVisible, setDrawOfferVisible] = useState(false);

  const router = useRouter();
  gameId = router.query.gameid;
  playerColor = router.query.currentPlayerColor;

  const handleOk = () => {
    setVisible(false);
    router.push("/game/" + gameId);
  };

  const offerDraw = () => {
    drawOffered = playerColor;
    axios.put(
            SERVER_ENDPOINT + "/move/" + gameId,
            {
              from: "",
              to: "",
              color: "",
              fen: game.fen(),
              drawState : "OFFERED"
            },
            { headers }
          )
  }

  const acceptDraw = () => {
    axios.put(
      SERVER_ENDPOINT + "/move/" + gameId,
      {
        from: "",
        to: "",
        color: "",
        fen: game.fen(),
        drawState : "ACCEPTED"
      },
      { headers }
    )
    setDrawOfferVisible(false);
  }

  const resign = () => {
    axios.put(
      SERVER_ENDPOINT + "/move/" + gameId,
      {
        from: "",
        to: "",
        color: "",
        fen: game.fen(),
        resign : true
      },
      { headers }
    )
  }

  

  useEffect(() => {
    if (called) return;
      called = true;

    token = getAuthToken();
    setHeaders({ Authorization: `Bearer ${token}` });

    var socket = new SockJS(SERVER_ENDPOINT + "/gs-guide-websocket");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
      var path = "/topic/move/" + gameId;
      stompClient.subscribe(path, function (greeting) {
        const move = JSON.parse(greeting.body);
        if (move.drawState == "OFFERED") {
          console.log(`${drawOffered} - ${playerColor}  =====================================`)
          if (drawOffered != playerColor) 
            setDrawOfferVisible(true)
          else
            drawOffered = "null";
        }
        if (move.color != playerColor) {
          makeAMove(
            {
              from: move.from,
              to: move.to,
              promotion: "q", // always promote to a queen for example simplicity
            },
            move.fen
          );
        }
        if (move.status != "active") {
          setGameStatus(move.message);
          setVisible(true);
        }
      });
    });
    // get fen from backend
    axios
      .get(SERVER_ENDPOINT + "/game/" + gameId + "/getFen", { headers })
      .then((res) => {
        const gameCopy = new Chess();
        gameCopy.load(res.data);
        setGame(gameCopy);
        if (res.data.split()[1] == "w" ? setTurn("white") : setTurn("black"));
        console.log(res.data);
      });
  }, []);

  useEffect(() => {}, [turn]);

  function makeAMove(move, fen) {
    try {
      const gameCopy = new Chess();
      gameCopy.load(fen);
      const result = gameCopy.move(move);
      setGame(gameCopy);
      const tempTurn = fen.split(" ")[1];
      if (result != null) {
        setTurn(tempTurn == "w" ? "black" : "white");
        let path = "/app/move/" + gameId;

        axios
          .put(
            SERVER_ENDPOINT + "/move/" + gameId,
            {
              from: move.from,
              to: move.to,
              color: playerColor,
              fen: game.fen(),
            },
            { headers }
          )
          .then((res) => {
            console.log(res);
            axios
              .put(
                SERVER_ENDPOINT + "/game/" + gameId + "/moved",
                {
                  from: move.from,
                  to: move.to,
                  color: playerColor,
                  fen: gameCopy.fen(),
                  drawState : "NULL"
                },
                { headers }
              )
              .then((res) => console.log(res))
              .catch((err) => console.log(err));
          })
          .catch((err) => console.log(err));
      }
      return result; // null if the move was illegal, the move object if the move was legal
    }
    catch {
      console.log("INVALID MOVE");
    }
  }

  function onDrop(sourceSquare, targetSquare) {
    if (game.get(sourceSquare).color != playerColor[0]) return false;
    const move = makeAMove(
      {
        from: sourceSquare,
        to: targetSquare,
        promotion: "q", // always promote to a queen for example simplicity
      },
      game.fen()
    );
    if (move === null) return false;
      return true;
  }

  return (
    <div>
    <div
      style={{
        width: "700px",
      }}
    >
      {/* <h2> Current player turn: {turn} </h2> */}
      <Chessboard
        alignContent="center"
        position={game.fen()}
        onPieceDrop={onDrop}
        boardOrientation={playerColor}
        // customDarkSquareStyle={{ borderRadius: '5px'}}
        customPieces={{
          bK: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "10px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Chess_kdt45.svg/90px-Chess_kdt45.svg.png" />
            </div>
          ),
          wP: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Chess_plt45.svg/90px-Chess_plt45.svg.png" />{" "}
            </div>
          ),
          bP: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Chess_pdt45.svg/90px-Chess_pdt45.svg.png" />
            </div>
          ),
          wN: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Chess_nlt45.svg/90px-Chess_nlt45.svg.png" />
            </div>
          ),
          bN: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Chess_ndt45.svg/90px-Chess_ndt45.svg.png" />{" "}
            </div>
          ),
          bR: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Chess_rdt45.svg/90px-Chess_rdt45.svg.png" />
            </div>
          ),
          wR: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Chess_rlt45.svg/90px-Chess_rlt45.svg.png" />{" "}
            </div>
          ),
          bB: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Chess_bdt45.svg/90px-Chess_bdt45.svg.png" />{" "}
            </div>
          ),
          wB: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Chess_blt45.svg/90px-Chess_blt45.svg.png" />{" "}
            </div>
          ),
          wQ: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Chess_qlt45.svg/90px-Chess_qlt45.svg.png" />{" "}
            </div>
          ),
          bQ: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Chess_qdt45.svg/90px-Chess_qdt45.svg.png" />{" "}
            </div>
          ),
          wK: ({ squareWidth = "32px" }) => (
            <div
              style={{
                fontSize: "90px",
              }}
            >
              {" "}
              <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Chess_klt45.svg/90px-Chess_klt45.svg.png" />{" "}
            </div>
          ),
        }}
      />

      <Modal
        id = "modal"
        title="Game Over"
        open={visible}
        onOk={handleOk}
        footer={[
          <Button key="submit" type="primary" onClick={handleOk}>
            OK{" "}
          </Button>,
        ]}
      >
        {gameStatus}
      </Modal>
      <Modal
        id = "modal"
        title="Draw offer"
        open={drawOfferVisible}
        footer={[
          <Button key="submit" type="primary" onClick={acceptDraw}>
            Accept{" "}
          </Button>,
          <Button key="submit" type="primary" onClick={() => setDrawOfferVisible(false)}>
          Decline{" "}
        </Button>
        ]}
      >
        Your opponent is offering a draw
      </Modal>
    </div>
    <button onClick={offerDraw} style = {{padding : "10px", marginTop : "15px", margin : "3px" , width : "20%"}}>
      Draw 🤝
    </button>
    <button onClick={resign} style = {{padding : "10px", marginTop : "15px", margin : "3px",  width : "20%"}}>
      Resign 🏳️
    </button>
    </div>

  );
}
PlayGame.getInitialProps = async ({ query }) => {
  const player1 = query.player1;
  const player2 = query.player2;
  const gameid = query.gameid;
  const playerColor = query.currentPlayerColor;
  return {
    player1,
    player2,
    gameid,
    playerColor,
  };
};
