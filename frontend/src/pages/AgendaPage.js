import React from 'react';
import { Box, Typography } from '@mui/material';
import AgendaList from '../components/Agenda/AgendaList';

const AgendaPage = () => {
  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Agenda Management
      </Typography>
      <AgendaList />
    </Box>
  );
};

export default AgendaPage;
