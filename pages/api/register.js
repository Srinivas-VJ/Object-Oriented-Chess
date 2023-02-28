import { SERVER_ENDPOINT } from "../../config";
import axios from "axios"

export default async function handler(req, res) {
    console.log(req.body);
    const response = await axios.post(SERVER_ENDPOINT + "/auth/register", req.body);
    res.status(response.status).json(response.data);
}
  