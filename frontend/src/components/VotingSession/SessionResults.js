import React, { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  Paper,
  CircularProgress,
  Button,
  Divider,
  Alert,
  Chip,
  Card,
  CardContent,
} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";
import PieChartIcon from "@mui/icons-material/PieChart";
import TimerIcon from "@mui/icons-material/Timer";
import { getSessionResults } from "../../services/sessionService";

const SessionResults = () => {
  const { sessionId } = useParams();
  const navigate = useNavigate();
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sessionStatus, setSessionStatus] = useState("LOADING");
  const [pollInterval, setPollInterval] = useState(null);

  const fetchResults = useCallback(async () => {
    try {
      const data = await getSessionResults(sessionId);
      setResults(data);

      if (data.result) {
        setSessionStatus("CLOSED");
        if (pollInterval) {
          clearInterval(pollInterval);
          setPollInterval(null);
        }
      } else {
        setSessionStatus("OPEN");
      }

      setError(null);
    } catch (err) {
      if (
        err.response?.data?.message?.includes("still open") ||
        err.response?.status === 400
      ) {
        setSessionStatus("OPEN");
        setError(
          "Session is still open for voting. Results will be available when voting ends."
        );
      } else {
        setSessionStatus("ERROR");
        setError("Failed to load voting results.");
      }
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [sessionId, pollInterval]);

  useEffect(() => {
    fetchResults();

    const interval = setInterval(() => {
      fetchResults();
    }, 5000);

    setPollInterval(interval);

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [fetchResults]);

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (sessionStatus === "OPEN") {
    return (
      <Box>
        <Paper elevation={3} sx={{ p: 4 }}>
          <Box sx={{ mb: 3 }}>
            <Typography variant="h5" component="h2" gutterBottom>
              Voting in Progress
            </Typography>
          </Box>

          <Card
            sx={{ mb: 3, bgcolor: "info.light", color: "info.contrastText" }}
          >
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <TimerIcon sx={{ mr: 2 }} />
                <Typography variant="h6">
                  Waiting for Session to Close
                </Typography>
              </Box>

              <Typography variant="body1" gutterBottom>
                Thank you for your vote! The voting session is still open.
                Results will be available once the voting period ends.
              </Typography>

              <Box sx={{ mt: 3, display: "flex", justifyContent: "center" }}>
                <CircularProgress size={24} sx={{ mr: 1 }} />
                <Typography>Refreshing automatically...</Typography>
              </Box>
            </CardContent>
          </Card>

          <Box sx={{ mt: 3, display: "flex", justifyContent: "space-between" }}>
            <Button variant="outlined" onClick={() => navigate("/agendas")}>
              Back to Agendas
            </Button>

            <Button variant="contained" onClick={fetchResults}>
              Refresh Results
            </Button>
          </Box>
        </Paper>
      </Box>
    );
  }

  if (error && sessionStatus === "ERROR") {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  const getResultChip = () => {
    if (!results.result) {
      return (
        <Chip
          label="Voting in progress"
          color="primary"
          icon={<PieChartIcon />}
          sx={{ fontSize: "1rem", py: 1 }}
        />
      );
    }

    return results.result === "Approved" ? (
      <Chip
        label="Approved"
        color="success"
        icon={<CheckCircleIcon />}
        sx={{ fontSize: "1rem", py: 1 }}
      />
    ) : (
      <Chip
        label="Rejected"
        color="error"
        icon={<CancelIcon />}
        sx={{ fontSize: "1rem", py: 1 }}
      />
    );
  };

  const yesPercentage =
    results.totalVotes > 0
      ? Math.round((results.yesVotes / results.totalVotes) * 100)
      : 0;

  const noPercentage =
    results.totalVotes > 0
      ? Math.round((results.noVotes / results.totalVotes) * 100)
      : 0;

  return (
    <Box>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Box
          sx={{
            mb: 3,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography variant="h5" component="h2" gutterBottom>
            Voting Results
          </Typography>
          {getResultChip()}
        </Box>

        <Box sx={{ mb: 3 }}>
          <Typography variant="h6" gutterBottom>
            {results.agendaTitle}
          </Typography>
          <Typography variant="subtitle1" color="text.secondary" gutterBottom>
            Session ID: {results.sessionId}
          </Typography>
        </Box>

        <Divider sx={{ mb: 3 }} />

        <Box sx={{ mb: 4 }}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              mb: 1,
            }}
          >
            <Typography variant="body1">Yes Votes:</Typography>
            <Typography variant="body1" fontWeight="bold">
              {results.yesVotes} ({yesPercentage}%)
            </Typography>
          </Box>

          <Box
            sx={{
              width: "100%",
              height: 20,
              bgcolor: "grey.200",
              borderRadius: 1,
              mb: 2,
            }}
          >
            <Box
              sx={{
                width: `${yesPercentage}%`,
                height: "100%",
                bgcolor: "success.main",
                borderRadius: 1,
              }}
            />
          </Box>

          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              mb: 1,
            }}
          >
            <Typography variant="body1">No Votes:</Typography>
            <Typography variant="body1" fontWeight="bold">
              {results.noVotes} ({noPercentage}%)
            </Typography>
          </Box>

          <Box
            sx={{
              width: "100%",
              height: 20,
              bgcolor: "grey.200",
              borderRadius: 1,
            }}
          >
            <Box
              sx={{
                width: `${noPercentage}%`,
                height: "100%",
                bgcolor: "error.main",
                borderRadius: 1,
              }}
            />
          </Box>
        </Box>

        <Divider sx={{ mb: 3 }} />

        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography variant="body1" fontWeight="bold">
            Total Votes: {results.totalVotes}
          </Typography>

          {!results.result && (
            <Typography variant="body2" color="text.secondary">
              Waiting for session to close...
            </Typography>
          )}
        </Box>

        <Box sx={{ mt: 4 }}>
          <Button
            variant="outlined"
            onClick={() => navigate("/agendas")}
            fullWidth
          >
            Back to Agendas
          </Button>
        </Box>
      </Paper>
    </Box>
  );
};

export default SessionResults;
