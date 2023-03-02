import React from "react";
import { getUserDetails } from "../utils/authenticate";

const user = getUserDetails();
console.log(user);
const LandingPage = () => (
  <div
    className="container"
    style={{
      backgroundImage: `url(https://wallpapercave.com/wp/wp2883302.jpg)`,
      backgroundSize: "cover",
    }}
  >
    <h1 className="title">Welcome to the Chess App</h1>
    <p className="description">
      Ready to test your chess skills? Start a new game and challenge yourself!
    </p>
    <div className="cta-container">
      <a href="/newgame" className="cta-button">
        Start a new game
      </a>
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

export default LandingPage;
