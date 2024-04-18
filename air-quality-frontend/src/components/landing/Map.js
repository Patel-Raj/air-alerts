import { Box, Container, Typography } from "@mui/material";
import React from "react";
import MapImage from "../../assets/images/map.png";

function Map() {
  return (
    <Container sx={{ width: "50vw", marginLeft: "150px" }}>
      <Box>
        <Typography
          variant="h2"
          sx={{ fontSize: "2.5rem", textAlign: "center", color: "#007bff" }}
        >
          Real Time Air Quality Alerts in Your Inbox
        </Typography>
      </Box>
      <Box marginTop={"20px"}>
        <img
          src={MapImage}
          alt="map"
          style={{ maxWidth: "900px", height: "500px", borderRadius: "10px" }}
        ></img>
      </Box>
    </Container>
  );
}

export default Map;
