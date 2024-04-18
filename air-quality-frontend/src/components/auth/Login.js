import { Box, Button, CircularProgress, Stack, TextField } from "@mui/material";
import React, { useState } from "react";
import { useAuth } from "../../utils/auth";
import { loginUser } from "../../services/api";
import { useNavigate } from "react-router-dom";
import Popup from "../common/Popup";

function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [circularProgress, setCircularProgress] = useState(false);

  const [popupContent, setPopupContent] = useState({
    open: false,
    message: "",
    closeMessage: "",
  });

  const auth = useAuth();

  const handleLogin = async () => {
    setCircularProgress(true);
    const userData = { email: email, password: password };
    const response = await loginUser(userData);
    setCircularProgress(false);
    if (!response.success) {
      setPopupContent({
        open: true,
        message: response.message,
        closeMessage: "Close",
      });
    } else {
      auth.login(response.token);
      navigate("/home");
    }
  };

  const handlePopupClose = () => {
    setPopupContent({ open: false, message: "", closeMessage: "" });
    navigate("/");
  };

  return (
    <Stack spacing={3}>
      <TextField
        id="outlined-basic"
        label="Email"
        variant="outlined"
        onChange={(e) => setEmail(e.target.value)}
      />
      <TextField
        id="outlined-basic"
        label="Password"
        variant="outlined"
        type="password"
        onChange={(e) => setPassword(e.target.value)}
      />
      <Box sx={{ textAlign: "center" }}>
        {!circularProgress ? (
          <Button variant="contained" onClick={handleLogin}>
            Login
          </Button>
        ) : (
          <CircularProgress />
        )}
      </Box>
      <Popup
        popupContent={popupContent}
        setPopupContent={setPopupContent}
        handlePopupClose={handlePopupClose}
      />
    </Stack>
  );
}

export default Login;
