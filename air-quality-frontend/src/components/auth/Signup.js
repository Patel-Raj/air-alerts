import { Box, Button, CircularProgress, Stack, TextField } from "@mui/material";
import { useRef, useState } from "react";
import Popup from "../common/Popup";
import { validateSignupForm } from "../../utils/validations";
import { registerUser } from "../../services/api";
import { useNavigate } from "react-router-dom";

function Signup({ backToLogin }) {
  const navigate = useNavigate();
  const nameRef = useRef(null);
  const emailRef = useRef(null);
  const passwordRef = useRef(null);
  const confirmPasswordRef = useRef(null);
  const [circularProgress, setCircularProgress] = useState(false);

  const [popupContent, setPopupContent] = useState({
    open: false,
    message: "",
    closeMessage: "",
  });

  const handleSignup = async (e) => {
    setCircularProgress(true);
    const data = {
      name: nameRef.current.value,
      email: emailRef.current.value,
      password: passwordRef.current.value,
      confirmPassword: confirmPasswordRef.current.value,
    };

    const result = validateSignupForm(data);
    if (!result.status) {
      setPopupContent({
        open: true,
        message: result.message,
        closeMessage: "Close",
      });
      return;
    }

    const response = await registerUser(data);
    setCircularProgress(false);
    if (!response.success) {
      setPopupContent({
        open: true,
        message: response.message,
        closeMessage: "Close",
      });
    } else {
      setPopupContent({
        open: true,
        message: response.message,
        closeMessage: "Close",
      });
    }
  };

  const handlePopupClose = () => {
    setPopupContent({ open: false, message: "", closeMessage: "" });
    backToLogin();
    navigate("/");
  };

  return (
    <Stack spacing={3}>
      <TextField
        id="outlined-basic"
        label="Name"
        variant="outlined"
        inputRef={nameRef}
      />
      <TextField
        id="outlined-basic"
        label="Email"
        variant="outlined"
        inputRef={emailRef}
      />
      <TextField
        id="outlined-basic"
        label="Password"
        variant="outlined"
        type="password"
        inputRef={passwordRef}
      />
      <TextField
        id="outlined-basic"
        label="Confirm Password"
        variant="outlined"
        type="password"
        inputRef={confirmPasswordRef}
      />
      <Box sx={{ textAlign: "center" }}>
        {!circularProgress ? (
          <Button variant="contained" onClick={handleSignup}>
            Signup
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

export default Signup;
