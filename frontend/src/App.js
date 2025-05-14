import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Layout from './components/UI/Layout';
import Home from './pages/Home';
import AgendaPage from './pages/AgendaPage';
import CreateAgendaPage from './pages/CreateAgendaPage';
import VotingPage from './pages/VotingPage';
import ResultsPage from './pages/ResultsPage';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/agendas" element={<AgendaPage />} />
            <Route path="/agendas/create" element={<CreateAgendaPage />} />
            <Route path="/vote/:sessionId" element={<VotingPage />} />
            <Route path="/results/:sessionId" element={<ResultsPage />} />
          </Routes>
        </Layout>
      </Router>
    </ThemeProvider>
  );
}

export default App;
