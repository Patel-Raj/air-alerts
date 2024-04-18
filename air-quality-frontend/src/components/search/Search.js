import {
  Box,
  CircularProgress,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { getCitiesOpenAQ, getCountriesOpenAQ } from "../../services/api";
import Popup from "../common/Popup";
import Parameters from "./Parameters";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../utils/auth";

function Search() {
  const navigate = useNavigate();
  const auth = useAuth();
  const [masterCountryList, setMasterCountryList] = useState([]);
  const [filteredCountryList, setFilteredCountryList] = useState([]);
  const [cityList, setCityList] = useState([]);
  const [errorPopup, setErrorPopup] = useState({
    open: false,
    message: "",
    closeMessage: "",
  });
  const [countryDataLoading, setCountryDataLoading] = useState(false);
  const [cityDataLoading, setCityDataLoading] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState(null);
  const [selectedCity, setSelectedCity] = useState(null);

  const getCountries = async () => {
    setCountryDataLoading(true);
    const result = await getCountriesOpenAQ();
    setCountryDataLoading(false);
    if (!result.success) {
      setErrorPopup({
        open: true,
        message: result.message,
        closeMessage: "Refresh",
      });
    } else {
      setMasterCountryList(result.data);
      setFilteredCountryList(result.data);
    }
  };

  useEffect(() => {
    getCountries();
  }, []);

  const getCities = async (country) => {
    setCityList([]);
    setCityDataLoading(true);
    const result = await getCitiesOpenAQ(country);
    setCityDataLoading(false);
    if (!result.success) {
      setErrorPopup({
        open: true,
        message: result.message,
        closeMessage: "Refresh",
      });
    } else {
      setCityList(result.data);
    }
  };

  useEffect(() => {
    if (selectedCountry === null) {
      return;
    }
    getCities(selectedCountry);
  }, [selectedCountry]);

  const handleErrorPopupClose = () => {
    window.location.reload(true);
  };

  const handleCountryFilter = (e) => {
    setSelectedCountry(null);
    setSelectedCity(null);
    if (e.target.value === null || e.target.value === "") {
      setFilteredCountryList(masterCountryList);
      return;
    }
    const searchTerm = e.target.value;
    let updateCountries = [];
    for (let i = 0; i < masterCountryList.length; i++) {
      if (
        masterCountryList[i].name
          .toLowerCase()
          .includes(searchTerm.toLowerCase())
      ) {
        updateCountries.push(masterCountryList[i]);
      }
    }
    setFilteredCountryList(updateCountries);
  };

  const handleCountrySelect = (item) => {
    setSelectedCountry(item);
    setSelectedCity(null);
  };

  const handleCitySelect = (item) => {
    setSelectedCity(item);
  };

  return (
    <Stack spacing={2} sx={{ marginTop: "30px" }}>
      <Box component="section" sx={{ paddingLeft: "20px" }}>
        <TextField
          placeholder="Search Country"
          onChange={handleCountryFilter}
        ></TextField>
      </Box>
      <Stack spacing={4} direction={"row"}>
        <Box component="section" sx={{ paddingLeft: "20px" }}>
          <Typography variant={"h5"}>Country</Typography>
          <Box
            sx={{
              width: "20vw",
              overflowY: "auto",
              height: "110vh",
            }}
          >
            {filteredCountryList.map((item) => (
              <Box
                key={item.code}
                sx={{
                  border: "1px solid #ccc",
                  borderRadius: "5px",
                  padding: "10px",
                  marginTop: "5px",
                  cursor: "pointer",
                  backgroundColor:
                    selectedCountry === item ? "rgb(0,123,250)" : "transparent",
                }}
                onClick={() => handleCountrySelect(item)}
              >
                <Typography>{item.name}</Typography>
              </Box>
            ))}
            {countryDataLoading && <CircularProgress />}
          </Box>
        </Box>
        {selectedCountry && (
          <Box component="section">
            <Box component="section" sx={{ paddingLeft: "20px" }}>
              <Typography variant={"h5"}>City</Typography>
              <Box
                sx={{
                  width: "20vw",
                  overflowY: "auto",
                  height: "110vh",
                }}
              >
                {cityList.map((item) => (
                  <Box
                    key={item.code}
                    sx={{
                      border: "1px solid #ccc",
                      borderRadius: "5px",
                      padding: "10px",
                      marginTop: "5px",
                      cursor: "pointer",
                      backgroundColor:
                        selectedCity === item
                          ? "rgb(0,123,250)"
                          : "transparent",
                    }}
                    onClick={() => handleCitySelect(item)}
                  >
                    <Typography>{item.city}</Typography>
                  </Box>
                ))}
                {cityDataLoading && <CircularProgress />}
              </Box>
            </Box>
          </Box>
        )}
        <Box component="section">
          {selectedCity && <Parameters location={selectedCity} />}
        </Box>
        <Popup
          popupContent={errorPopup}
          setPopupContent={setErrorPopup}
          handlePopupClose={handleErrorPopupClose}
        />
      </Stack>
    </Stack>
  );
}

export default Search;
