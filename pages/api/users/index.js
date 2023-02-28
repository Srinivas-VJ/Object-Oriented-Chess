import { SERVER_ENDPOINT } from "../../../config";
import axios from "axios"

export default async function handler(req, res) {
    const response = await axios.get(SERVER_ENDPOINT + "/users");
    res.status(response.status).json(response.data);
}
  