import React from 'react';
import { Box, Typography } from '@mui/material';
import AgendaForm from '../components/Agenda/AgendaForm';

const CreateAgendaPage = () => {
  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Create New Agenda
      </Typography>
      <AgendaForm />
    </Box>
  );
};

export default CreateAgendaPage;
