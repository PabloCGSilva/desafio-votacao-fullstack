import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Box,
  Typography,
  Paper,
  Button,
  TextField,
  FormControl,
  FormLabel,
  RadioGroup,
  FormControlLabel,
  Radio,
  CircularProgress,
  Snackbar,
  Alert,
  LinearProgress,
  Stepper,
  Step,
  StepLabel,
} from "@mui/material";
import { castVote } from "../../services/voteService";
import { getSessionResults } from "../../services/sessionService";
import api from "../../services/api";

const VoteForm = () => {
  const { sessionId } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    associateId: "",
    cpf: "",
    voteOption: true,
    sessionId: sessionId,
  });
  const [loading, setLoading] = useState(false);
  const [validateLoading, setValidateLoading] = useState(false);
  const [validated, setValidated] = useState(false);
  const [sessionInfo, setSessionInfo] = useState(null);
  const [sessionLoading, setSessionLoading] = useState(true);
  const [timeLeft, setTimeLeft] = useState(null);
  const [alert, setAlert] = useState({
    open: false,
    message: "",
    severity: "success",
  });
  const [errors, setErrors] = useState({});
  const [activeStep, setActiveStep] = useState(0);

  useEffect(() => {
    const fetchSessionData = async () => {
      try {
        setSessionLoading(true);
        const results = await getSessionResults(sessionId);
        if (results.result) {
          navigate(`/results/${sessionId}`);
          return;
        }
        setSessionInfo({
          id: sessionId,
          status: "OPEN",
        });

        setTimeLeft(5 * 60);
      } catch (error) {
        setSessionInfo({
          id: sessionId,
          status: "OPEN",
        });
        setTimeLeft(5 * 60);
      } finally {
        setSessionLoading(false);
      }
    };

    fetchSessionData();
  }, [sessionId, navigate]);

  useEffect(() => {
    if (timeLeft === null) return;

    if (timeLeft <= 0) {
      navigate(`/results/${sessionId}`);
      return;
    }

    const timer = setInterval(() => {
      setTimeLeft((prev) => prev - 1);
    }, 1000);

    return () => clearInterval(timer);
  }, [timeLeft, sessionId, navigate]);

  const formatTimeLeft = () => {
    if (!timeLeft && timeLeft !== 0) return "--:--";
    const minutes = Math.floor(timeLeft / 60);
    const seconds = timeLeft % 60;
    return `${minutes.toString().padStart(2, "0")}:${seconds
      .toString()
      .padStart(2, "0")}`;
  };

  const handleChange = (e) => {
    const { name, value, type } = e.target;

    if (type === "radio") {
      setFormData({
        ...formData,
        [name]: value === "true",
      });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }

    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: "",
      });
    }

    if (name === "associateId" || name === "cpf") {
      setValidated(false);
      setActiveStep(0);
    }
  };

  const validateIdentity = async () => {
    const newErrors = {};

    if (!formData.associateId.trim()) {
      newErrors.associateId = "Associate ID is required";
    }

    if (!formData.cpf.trim()) {
      newErrors.cpf = "CPF is required";
    } else if (!/^\d{11}$/.test(formData.cpf)) {
      newErrors.cpf = "CPF must contain 11 digits";
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) {
      return;
    }

    setValidateLoading(true);
    try {
      await api.get(
        `/votes/validate?cpf=${formData.cpf}&associateId=${formData.associateId}&sessionId=${sessionId}`
      );

      setValidated(true);
      setActiveStep(1);
      setAlert({
        open: true,
        message: "Identity verified. You can now vote.",
        severity: "success",
      });
    } catch (error) {
      let errorMessage = "Failed to validate identity";

      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.response?.status === 403) {
        errorMessage = "You are not eligible to vote";
      } else if (error.response?.status === 409) {
        errorMessage = "You have already voted in this session";
      }

      setAlert({
        open: true,
        message: errorMessage,
        severity: "error",
      });
    } finally {
      setValidateLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validated) {
      await validateIdentity();
      return;
    }

    try {
      setLoading(true);
      await castVote(formData);

      setAlert({
        open: true,
        message: "Vote registered successfully!",
        severity: "success",
      });

      setTimeout(() => {
        navigate("/agendas");
      }, 2000);
    } catch (error) {
      let errorMessage = "Failed to cast vote";

      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.response?.status === 409) {
        errorMessage = "You have already voted in this session";
      } else if (error.response?.status === 400) {
        errorMessage = "Invalid vote data";
      } else if (error.response?.status === 403) {
        errorMessage = "You are not eligible to vote";
      }

      setAlert({
        open: true,
        message: errorMessage,
        severity: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  if (sessionLoading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Box sx={{ mb: 3 }}>
          <Typography variant="h5" component="h2" gutterBottom>
            Voting Session
          </Typography>

          <Box sx={{ mb: 2 }}>
            <Typography variant="body1" color="primary" gutterBottom>
              Time remaining: {formatTimeLeft()}
            </Typography>
            <LinearProgress
              variant="determinate"
              value={((5 * 60 - timeLeft) / (5 * 60)) * 100}
              sx={{ mt: 1 }}
            />
          </Box>
        </Box>

        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          <Step>
            <StepLabel>Validate Identity</StepLabel>
          </Step>
          <Step>
            <StepLabel>Cast Your Vote</StepLabel>
          </Step>
        </Stepper>

        <Box component="form" onSubmit={handleSubmit} noValidate>
          <TextField
            margin="normal"
            required
            fullWidth
            id="associateId"
            label="Associate ID"
            name="associateId"
            value={formData.associateId}
            onChange={handleChange}
            disabled={loading || validateLoading || validated}
            error={!!errors.associateId}
            helperText={errors.associateId}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            id="cpf"
            label="CPF (11 digits)"
            name="cpf"
            value={formData.cpf}
            onChange={handleChange}
            disabled={loading || validateLoading || validated}
            error={!!errors.cpf}
            helperText={errors.cpf}
          />

          {validated && (
            <FormControl component="fieldset" sx={{ mt: 2 }}>
              <FormLabel component="legend">Your Vote</FormLabel>
              <RadioGroup
                row
                name="voteOption"
                value={formData.voteOption.toString()}
                onChange={handleChange}
              >
                <FormControlLabel
                  value="true"
                  control={<Radio />}
                  label="Yes"
                />
                <FormControlLabel
                  value="false"
                  control={<Radio />}
                  label="No"
                />
              </RadioGroup>
            </FormControl>
          )}

          <Box sx={{ mt: 3 }}>
            {!validated ? (
              <Button
                type="button"
                fullWidth
                variant="contained"
                onClick={validateIdentity}
                disabled={loading || validateLoading}
              >
                {validateLoading ? (
                  <CircularProgress size={24} />
                ) : (
                  "Validate Identity"
                )}
              </Button>
            ) : (
              <Button
                type="submit"
                fullWidth
                variant="contained"
                disabled={loading}
              >
                {loading ? <CircularProgress size={24} /> : "Cast Vote"}
              </Button>
            )}
          </Box>
        </Box>
      </Paper>

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

export default VoteForm;
