import { Grid, Stack } from "@mui/material";
import React from "react";
import Map from "../components/landing/Map";
import Auth from "../components/auth/Auth";

function Landing() {
  return (
    <Stack spacing={10} sx={{ marginTop: "100px" }}>
      <Grid container>
        <Grid item xs={6}>
          <Map />
        </Grid>
        <Grid item xs={6}>
          <Auth />
        </Grid>
      </Grid>
    </Stack>
  );
}

export default Landing;
