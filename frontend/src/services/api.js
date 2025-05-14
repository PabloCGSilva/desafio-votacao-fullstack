import axios from "axios";

const API_BASE_URL =
  process.env.NODE_ENV === "production"
    ? "/api/v1"
    : "http://localhost:8080/api/v1";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
