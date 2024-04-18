import {
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

function SubscribePopup({
  popupContent,
  handlePopupClose,
  refreshAfterSubscribe,
}) {
  const [subscribeTask, setSubscribeTask] = useState(false);
  const minutesRef = useRef();
  const navigate = useNavigate();

  const handleSubscribe = async (parameter) => {
    setSubscribeTask(true);
    navigate("/checkout", {
      state: { parameter: parameter, minutes: minutesRef.current.value },
    });
    setSubscribeTask(false);
  };

  return (
    <Dialog
      open={true}
      keepMounted
      onClose={handlePopupClose}
      aria-describedby="alert-dialog-slide-description"
      PaperProps={{ sx: { width: "50vw" } }}
    >
      <DialogTitle>Get Alerts</DialogTitle>
      <DialogContent dividers>
        <Grid container alignItems="center">
          <Grid item xs={12} sm={12}>
            <Stack direction="column" alignItems="flex-start" spacing={2}>
              <Typography>
                {popupContent.description} ( {popupContent.displayName} )
              </Typography>
              <Typography>
                Current reading: {popupContent.value} {popupContent.unit}
              </Typography>
              <Typography>Subscription Cost: $10 CAD</Typography>
              <Stack direction="row" alignItems={"center"} spacing={2}>
                <Typography>Every</Typography>
                <TextField
                  type="number"
                  inputRef={minutesRef}
                  InputLabelProps={{ shrink: true }}
                  InputProps={{
                    inputProps: { min: 1 },
                  }}
                  sx={{ width: "80px" }}
                />
                <Typography>Minutes</Typography>
                {!subscribeTask && (
                  <Button
                    variant="contained"
                    onClick={() => handleSubscribe(popupContent)}
                  >
                    Proceed to Pay
                  </Button>
                )}
                {subscribeTask && <CircularProgress />}
              </Stack>
            </Stack>
          </Grid>
        </Grid>
      </DialogContent>
      <DialogActions>
        <Button onClick={handlePopupClose}>Close</Button>
      </DialogActions>
    </Dialog>
  );
}

export default SubscribePopup;
