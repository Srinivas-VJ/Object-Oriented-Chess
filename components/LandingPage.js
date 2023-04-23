import React, { useEffect, useState } from "react";
import { getUserDetails } from "../utils/authenticate";
import Link from "next/link";
import { Modal, Button } from "antd";
import { SERVER_ENDPOINT } from "../config";
import { useRouter } from "next/router";

const user = getUserDetails();
var stompClient = null;
var called = false;

const LandingPage = () => {
  const [visible, setVisible] = useState(false);
  const [challengeMessage, setChallengeMessage] = useState("");
  const [game, setGame] = useState(null);
  const router = useRouter();

  const handleAccept = () => {
    const currentPlayerColor = game.playerBlack == user.username ? "BLACK" : "WHITE"
    const playerWhite = game.playerWhite
    const playerBlack = game.playerBlack
    const gameId = game.gameID
    router.push({
      pathname: `/newgame/${game.gameID}`,
      query: { playerWhite, playerBlack, gameId, currentPlayerColor},
    });
  }

  useEffect ( () => {

    if (called)
      return
    called = true;

    var socket = new SockJS(SERVER_ENDPOINT + "/gs-guide-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      var path = "/user/" + user.username + "/notification";
      stompClient.subscribe(path, function (notification) {
        const gameObj = JSON.parse(notification.body)
        setGame(gameObj)
        const opponent = gameObj.playerBlack == user.username ? gameObj.playerWhite : gameObj.playerBlack;
        setChallengeMessage(`${opponent} is challenging you to a game`);
        setVisible(true);
      });
    });
  }, [])


  return (
  <div
    className="container"
    style={{
      backgroundImage: `url(https://wallpapercave.com/wp/wp2883302.jpg)`,
      backgroundSize: "cover",
    }}
  >
     <Modal
        id = "modal"
        title="Notification"
        open={visible}
        footer={[
          <Button key="accept" type="primary" onClick={handleAccept}>
            Accept{" "}
          </Button>,
          <Button key="decline" type="secondary" onClick={() => setVisible(false)}>
          Decline{" "}
        </Button>,
        ]}
      >
        {challengeMessage}
      </Modal>

    <h1 className="title">Welcome to the Chess App</h1>
    <p className="description">
      Ready to test your chess skills? Start a new game and challenge other players!
    </p>
    <div className="cta-container">
      <Link href={"/newgame"}>
        <button className="cta-button">
        Start a new game
        </button>
      </Link>
    </div>

    <style jsx>{`
      .body {
      }
      .container {
        display: flex;
        flex-direction: column;
        align-items: center;
        height: 100vh;
      }

      .title {
        font-size: 48px;
        margin-bottom: 24px;
      }

      .description {
        font-size: 24px;
        margin-bottom: 48px;
        text-align: center;
        width: 80%;
      }

      .cta-container {
        display: flex;
        justify-content: center;
      }

      .cta-button {
        background-color: blue;
        border: none;
        color: white;
        padding: 16px 32px;
        text-align: center;
        text-decoration: none;
        display: inline-block;
        font-size: 24px;
        margin: 24px;
        cursor: pointer;
        border-radius: 4px;
      }
    `}</style>
  </div>
);
}

export default LandingPage;
