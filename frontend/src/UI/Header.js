import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import HowToVoteIcon from '@mui/icons-material/HowToVote';

const Header = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <HowToVoteIcon sx={{ mr: 2 }} />
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Cooperative Voting System
        </Typography>
        <Box>
          <Button color="inherit" component={RouterLink} to="/">
            Home
          </Button>
          <Button color="inherit" component={RouterLink} to="/agendas">
            Agendas
          </Button>
          <Button color="inherit" component={RouterLink} to="/agendas/create">
            Create Agenda
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
