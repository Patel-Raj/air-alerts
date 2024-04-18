import { Container, Grid, Typography, Button } from "@mui/material";
import React, { useState } from "react";
import Login from "./Login";
import Signup from "./Signup";

function Auth() {
  const [login, setLogin] = useState(true);

  const backToLogin = () => {
    setLogin(true);
  };

  return (
    <Container
      sx={{
        width: "500px",
        marginTop: "100px",
        paddingBottom: "20px",
      }}
    >
      <Grid container sx={{ marginBottom: "20px" }}>
        <Grid
          item
          xs={6}
          textAlign={"center"}
          sx={{
            backgroundColor: login ? "lightblue" : "",
            borderRadius: login ? "10px" : "",
            border: login ? "2px solid #007bff" : "",
          }}
        >
          <Button onClick={() => setLogin(true)}>
            <Typography>Login</Typography>
          </Button>
        </Grid>
        <Grid
          item
          xs={6}
          textAlign={"center"}
          sx={{
            backgroundColor: !login ? "lightblue" : "",
            borderRadius: !login ? "10px" : "",
            border: !login ? "2px solid #007bff" : "",
          }}
        >
          <Button onClick={() => setLogin(false)}>
            <Typography>Signup</Typography>
          </Button>
        </Grid>
      </Grid>
      {login && <Login />}
      {!login && <Signup backToLogin={backToLogin} />}
    </Container>
  );
}

export default Auth;
