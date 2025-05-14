import React from 'react';
import { Box, Typography } from '@mui/material';
import SessionResults from '../components/VotingSession/SessionResults';

const ResultsPage = () => {
  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Voting Results
      </Typography>
      <SessionResults />
    </Box>
  );
};

export default ResultsPage;
