import {
  Box,
  Button,
  CircularProgress,
  Grid,
  Stack,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { getParametersOpenAQ, unsubscribeOpenAQ } from "../../services/api";
import Popup from "../common/Popup";
import SubscribePopup from "./SubscribePopup";

function Parameters({ location }) {
  const [parameterList, setParameterList] = useState([]);
  const [parameterDataLoading, setParameterDataLoading] = useState(false);
  const [currentParameter, setCurrentParameter] = useState(null);
  const [updates, setUpdates] = useState([]);

  const [errorPopup, setErrorPopup] = useState({
    open: false,
    message: "",
    closeMessage: "",
  });

  const getParameters = async (city) => {
    setParameterList([]);
    setParameterDataLoading(true);
    const result = await getParametersOpenAQ(city);
    console.log(result);
    setParameterDataLoading(false);
    if (!result.success) {
      setErrorPopup({
        open: true,
        message: result.message,
        closeMessage: "Refresh",
      });
    } else {
      setParameterList(result.data);
    }
  };

  useEffect(() => {
    getParameters(location);
  }, [location, updates]);

  const handleGetAlerts = async (item) => {
    setCurrentParameter(item);
  };

  const handleUnsubscribe = async (item) => {
    const result = await unsubscribeOpenAQ(item.subscriptionId);
    console.log(result);
    if (!result.success) {
      setErrorPopup({
        open: true,
        message: result.message,
        closeMessage: "Refresh",
      });
    } else {
      setUpdates([...updates, result.data]);
    }
  };

  const handleErrorPopupClose = () => {
    window.location.reload(true);
  };

  const handleAlertPopupClose = () => {
    setCurrentParameter(null);
  };

  const refreshAfterSubscribe = (subscriptionId) => {
    setCurrentParameter(null);
    setUpdates([...updates, subscriptionId]);
  };

  return (
    <>
      <Box component="section">
        <Box component="section">
          <Box component="section" sx={{ paddingLeft: "20px" }}>
            <Typography variant={"h5"}>Parameters</Typography>
            <Box
              sx={{
                width: "50vw",
                overflowY: "auto",
                height: "110vh",
              }}
            >
              {parameterList.map((item) => (
                <Box
                  key={item.parameterId}
                  sx={{
                    border: "1px solid #ccc",
                    borderRadius: "5px",
                    padding: "10px",
                    marginTop: "5px",
                  }}
                >
                  <Grid container alignItems="center">
                    <Grid item xs={12} sm={"auto"}>
                      <Stack
                        direction="column"
                        alignItems="flex-start"
                        spacing={2}
                      >
                        <Typography>
                          {item.description} ( {item.displayName} )
                        </Typography>
                        <Typography>
                          Current reading: {item.value} {item.unit}
                        </Typography>
                      </Stack>
                    </Grid>
                    <Grid item xs={12} container sm justifyContent="flex-end">
                      {!item.subscribed && (
                        <Button
                          variant="contained"
                          onClick={() => handleGetAlerts(item)}
                        >
                          Get Alerts
                        </Button>
                      )}
                      {item.subscribed && (
                        <Button
                          variant="contained"
                          onClick={() => handleUnsubscribe(item)}
                          sx={{
                            backgroundColor: "#FF474C",
                            color: "white",
                            "&:hover": {
                              backgroundColor: "#FF474C",
                              color: "white",
                            },
                          }}
                        >
                          Unsubscribe
                        </Button>
                      )}
                    </Grid>
                  </Grid>
                </Box>
              ))}
              {parameterDataLoading && <CircularProgress />}
            </Box>
          </Box>
        </Box>
      </Box>
      <Popup
        popupContent={errorPopup}
        setPopupContent={setErrorPopup}
        handlePopupClose={handleErrorPopupClose}
      />
      {currentParameter && (
        <SubscribePopup
          popupContent={currentParameter}
          handlePopupClose={handleAlertPopupClose}
          refreshAfterSubscribe={refreshAfterSubscribe}
        />
      )}
    </>
  );
}

export default Parameters;
