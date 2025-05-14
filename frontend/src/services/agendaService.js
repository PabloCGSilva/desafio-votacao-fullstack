import api from './api';

export const createAgenda = async (agendaData) => {
  try {
    const response = await api.post('/agendas', agendaData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getAllAgendas = async () => {
  try {
    const response = await api.get('/agendas');
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getAgendaById = async (id) => {
  try {
    const response = await api.get(`/agendas/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};
