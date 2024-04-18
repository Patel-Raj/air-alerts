import { CircularProgress, Container } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Popup from "../common/Popup";
import { verifyUserBackend } from "../../services/api";

function VerifyEmail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [popupContent, setPopupContent] = useState({
    open: false,
    message: "",
    closeMessage: "",
  });
  const [dataLoading, setDataLoading] = useState(true);

  const verifyUser = async (id) => {
    const response = await verifyUserBackend(id);
    setDataLoading(false);
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
        closeMessage: "Login",
      });
    }
  };

  useEffect(() => {
    verifyUser(id);
  }, [id]);

  const handlePopupClose = () => {
    setPopupContent({ open: false, message: "", closeMessage: "" });
    navigate("/");
  };

  return (
    <Container>
      <Popup
        popupContent={popupContent}
        setPopupContent={setPopupContent}
        handlePopupClose={handlePopupClose}
      />
      {dataLoading && <CircularProgress />}
    </Container>
  );
}

export default VerifyEmail;
