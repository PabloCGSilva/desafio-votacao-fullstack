import React from 'react';
import { Typography, Button, Box, Paper, Grid } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import HowToVoteIcon from '@mui/icons-material/HowToVote';
import ListAltIcon from '@mui/icons-material/ListAlt';
import AddIcon from '@mui/icons-material/Add';

const Home = () => {
  return (
    <Box sx={{ textAlign: 'center', mt: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom>
        Welcome to the Cooperative Voting System
      </Typography>
      <Typography variant="h5" color="textSecondary" paragraph>
        Create agendas, open voting sessions, and cast your vote on cooperative matters
      </Typography>
      
      <Grid container spacing={3} sx={{ mt: 4, justifyContent: 'center' }}>
        <Grid item xs={12} sm={4}>
          <Paper 
            elevation={3} 
            sx={{ 
              p: 3, 
              height: '100%', 
              display: 'flex', 
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'space-between'
            }}
          >
            <Box sx={{ mb: 2 }}>
              <ListAltIcon color="primary" sx={{ fontSize: 60 }} />
              <Typography variant="h5" component="h2" gutterBottom>
                View Agendas
              </Typography>
              <Typography variant="body1" color="textSecondary">
                See all available agendas and their voting sessions
              </Typography>
            </Box>
            <Button 
              variant="contained" 
              component={RouterLink} 
              to="/agendas"
              sx={{ mt: 2 }}
            >
              View Agendas
            </Button>
          </Paper>
        </Grid>
        
        <Grid item xs={12} sm={4}>
          <Paper 
            elevation={3} 
            sx={{ 
              p: 3, 
              height: '100%', 
              display: 'flex', 
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'space-between'
            }}
          >
            <Box sx={{ mb: 2 }}>
              <AddIcon color="primary" sx={{ fontSize: 60 }} />
              <Typography variant="h5" component="h2" gutterBottom>
                Create Agenda
              </Typography>
              <Typography variant="body1" color="textSecondary">
                Create a new agenda for cooperative members to vote on
              </Typography>
            </Box>
            <Button 
              variant="contained" 
              component={RouterLink} 
              to="/agendas/create"
              sx={{ mt: 2 }}
            >
              Create Agenda
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Home;
