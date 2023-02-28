import { SERVER_ENDPOINT } from "../../config";
import axios from "axios"

export default async function handler(req, res) {
    const response = await axios.post(SERVER_ENDPOINT + "/auth/authenticate", req.body);
    res.status(response.status).json(response.data);
}
  