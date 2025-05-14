import api from './api';

export const castVote = async (voteData) => {
  try {
    const response = await api.post('/votes', voteData);
    return response.data;
  } catch (error) {
    throw error;
  }
};
