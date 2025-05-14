import api from "./api";

export const createVotingSession = async (sessionData) => {
  try {
    const response = await api.post("/sessions", sessionData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getSessionResults = async (id) => {
  try {
    const response = await api.get(`/sessions/${id}/results`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getActiveSessions = async () => {
  try {
    const response = await api.get("/sessions/active");
    return response.data;
  } catch (error) {
    throw error;
  }
};
