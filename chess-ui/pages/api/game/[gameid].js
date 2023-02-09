import { SERVER_ENDPOINT } from "../../../config";
import { useRouter } from "next/router";
import axios from "axios"

export default async function handler(req, res) {
    console.log("reached");
    console.log(req)
    const {gameid} = req.query;
    const response = await axios.get(SERVER_ENDPOINT + "/game/" + gameid);
    res.status(response.status).json(response.data);
}
  