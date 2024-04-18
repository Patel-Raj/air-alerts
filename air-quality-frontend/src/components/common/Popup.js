import {
  Button,
  Dialog,
  DialogActions,
  DialogTitle,
  Slide,
} from "@mui/material";
import React from "react";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

function Popup({ popupContent, setPopupContent, handlePopupClose }) {
  return (
    <Dialog
      open={popupContent.open}
      TransitionComponent={Transition}
      keepMounted
      onClose={handlePopupClose}
      aria-describedby="alert-dialog-slide-description"
    >
      <DialogTitle>{popupContent.message}</DialogTitle>
      <DialogActions>
        <Button onClick={handlePopupClose}>{popupContent.closeMessage}</Button>
      </DialogActions>
    </Dialog>
  );
}

export default Popup;
