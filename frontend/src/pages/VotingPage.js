import React from 'react';
import { Box, Typography } from '@mui/material';
import VoteForm from '../components/Vote/VoteForm';

const VotingPage = () => {
  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Cast Your Vote
      </Typography>
      <VoteForm />
    </Box>
  );
};

export default VotingPage;
