import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  Paper,
  List,
  ListItem,
  ListItemText,
  Button,
  Divider,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  TextField,
  Snackbar,
  Alert,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import HowToVoteIcon from "@mui/icons-material/HowToVote";
import { getAllAgendas } from "../../services/agendaService";
import {
  createVotingSession,
  getActiveSessions,
} from "../../services/sessionService";

const AgendaList = () => {
  const navigate = useNavigate();
  const [agendas, setAgendas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sessionDialog, setSessionDialog] = useState({
    open: false,
    agendaId: null,
    title: "",
  });
  const [sessionDuration, setSessionDuration] = useState(1);
  const [alert, setAlert] = useState({
    open: false,
    message: "",
    severity: "success",
  });
  const [sessionsMap, setSessionsMap] = useState({});

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const agendasData = await getAllAgendas();
        setAgendas(agendasData);

        const activeSessions = await getActiveSessions();
        const sessions = {};
        activeSessions.forEach((session) => {
          sessions[session.agendaId] = session.id;
        });
        setSessionsMap(sessions);

        setError(null);
      } catch (err) {
        setError("Failed to load data. Please try again later.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleCreateSessionClick = (agendaId, title) => {
    setSessionDialog({
      open: true,
      agendaId,
      title,
    });
  };

  const handleCloseDialog = () => {
    setSessionDialog({
      ...sessionDialog,
      open: false,
    });
    setSessionDuration(1);
  };

  const handleCreateSession = async () => {
    try {
      const response = await createVotingSession({
        agendaId: sessionDialog.agendaId,
        durationInMinutes: sessionDuration,
      });

      setAlert({
        open: true,
        message: "Voting session created successfully!",
        severity: "success",
      });

      handleCloseDialog();

      setSessionsMap((prev) => ({
        ...prev,
        [sessionDialog.agendaId]: response,
      }));

      navigate(`/vote/${response}`);
    } catch (error) {
      setAlert({
        open: true,
        message:
          error.response?.data?.message || "Failed to create voting session",
        severity: "error",
      });
    }
  };

  const handleVoteClick = (sessionId) => {
    navigate(`/vote/${sessionId}`);
  };

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 2,
        }}
      >
        <Typography variant="h5" component="h2">
          Available Agendas
        </Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => navigate("/agendas/create")}
        >
          Create Agenda
        </Button>
      </Box>

      {agendas.length === 0 ? (
        <Paper sx={{ p: 3, textAlign: "center" }}>
          <Typography variant="body1">
            No agendas available. Create one to start voting!
          </Typography>
        </Paper>
      ) : (
        <Paper elevation={3}>
          <List>
            {agendas.map((agenda, index) => (
              <React.Fragment key={agenda.id}>
                {index > 0 && <Divider />}
                <ListItem
                  alignItems="flex-start"
                  secondaryAction={
                    sessionsMap[agenda.id] ? (
                      <Button
                        variant="contained"
                        color="primary"
                        startIcon={<HowToVoteIcon />}
                        onClick={() => handleVoteClick(sessionsMap[agenda.id])}
                      >
                        Vote Now
                      </Button>
                    ) : (
                      <Button
                        variant="outlined"
                        color="primary"
                        startIcon={<HowToVoteIcon />}
                        onClick={() =>
                          handleCreateSessionClick(agenda.id, agenda.title)
                        }
                      >
                        Open Voting
                      </Button>
                    )
                  }
                >
                  <ListItemText
                    primary={agenda.title}
                    secondary={
                      <React.Fragment>
                        <Typography
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          {agenda.description}
                        </Typography>
                        {agenda.createdAt && (
                          <Typography
                            component="span"
                            variant="body2"
                            sx={{ display: "block", mt: 1 }}
                          >
                            Created:{" "}
                            {new Date(agenda.createdAt).toLocaleString()}
                          </Typography>
                        )}
                      </React.Fragment>
                    }
                  />
                </ListItem>
              </React.Fragment>
            ))}
          </List>
        </Paper>
      )}

      {/* Create Voting Session Dialog */}
      <Dialog open={sessionDialog.open} onClose={handleCloseDialog}>
        <DialogTitle>Open Voting Session</DialogTitle>
        <DialogContent>
          <DialogContentText>
            You are opening a voting session for agenda:{" "}
            <strong>{sessionDialog.title}</strong>
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="duration"
            label="Duration (minutes)"
            type="number"
            fullWidth
            variant="outlined"
            value={sessionDuration}
            onChange={(e) =>
              setSessionDuration(Math.max(1, parseInt(e.target.value) || 1))
            }
            inputProps={{ min: 1 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleCreateSession} variant="contained">
            Open Session
          </Button>
        </DialogActions>
      </Dialog>

      {/* Alert Snackbar */}
      <Snackbar
        open={alert.open}
        autoHideDuration={6000}
        onClose={() => setAlert({ ...alert, open: false })}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert severity={alert.severity} sx={{ width: "100%" }}>
          {alert.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default AgendaList;
