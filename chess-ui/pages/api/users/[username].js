import { SERVER_ENDPOINT } from "../../../config";
import axios from "axios"

export default async function handler(req, res) {
    const {username} = req.query;
    const response = await axios.get(SERVER_ENDPOINT + "/users/" + username);
    res.status(response.status).json(response.data);
}
  