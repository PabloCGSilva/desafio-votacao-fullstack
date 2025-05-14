import React from "react";
import { Box, Typography, Container, Link } from "@mui/material";

const Footer = () => {
  return (
    <Box
      component="footer"
      sx={{
        py: 3,
        px: 2,
        mt: "auto",
        backgroundColor: "primary.main",
        color: "white",
      }}
    >
      <Container maxWidth="sm">
        <Typography variant="body1" align="center">
          db1group© {new Date().getFullYear()} Cooperative Voting System
        </Typography>
        <Typography variant="body2" align="center" sx={{ mt: 1 }}>
          {"Built with "}
          <Link
            color="inherit"
            href="https://reactjs.org/"
            target="_blank"
            rel="noopener"
          >
            React
          </Link>
          {" and "}
          <Link
            color="inherit"
            href="https://mui.com/"
            target="_blank"
            rel="noopener"
          >
            Material UI
          </Link>
        </Typography>
      </Container>
    </Box>
  );
};

export default Footer;
