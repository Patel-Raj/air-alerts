import axios from "axios";
import {
  GET_CITIES_API,
  GET_COUNTRIES_API,
  GET_PARAMETERS_API,
  LOGIN_API,
  POST_UNSUBSCRIBE_API,
  REGISTER_API,
  STRIPE_CREATE_SESSION_API,
  VERIFY_EMAIL_API,
} from "../constants/api";
import { createCronSchedule, getIdFromToken } from "../utils/common";

export const registerUser = async (userData) => {
  let result = {
    success: false,
    message: "Server Error in Register user",
    data: {},
  };
  await axios
    .post(REGISTER_API, userData)
    .then((response) => {
      console.log(response);
      result = { success: true, message: "Verify your Email!" };
    })
    .catch((error) => {
      console.log(error);
      if (error.response.status === 500) {
        result = { success: false, message: "Server Error in Register user" };
      } else if (error.response.status === 404) {
        result = { success: false, message: "Server Down" };
      } else if (error.response.status === 409) {
        result = {
          success: false,
          message: "User already registed with Email: " + userData.email,
        };
      }
    });

  return result;
};

export const verifyUserBackend = async (id) => {
  let result = {
    success: false,
    message: "Server Error in verify user",
    data: {},
  };

  const data = { id: id };

  await axios
    .post(VERIFY_EMAIL_API, data)
    .then((response) => {
      console.log(response);
      if (response.data.status) {
        result = { success: response.data.status, message: "User verified" };
      } else {
        result = {
          success: response.data.status,
          message: response.data.message,
        };
      }
    })
    .catch((error) => {
      console.log(error);
      if (error.response.status === 500) {
        result = { success: false, message: "Server Error in Verify user" };
      } else if (error.response.status === 404) {
        result = { success: false, message: "Server Down" };
      }
    });

  return result;
};

export const loginUser = async (userData) => {
  let result = {
    success: false,
    message: "Server Error in Login user",
    data: {},
  };
  await axios
    .post(LOGIN_API, userData)
    .then((response) => {
      console.log(response);
      if (response.data.status) {
        result = {
          success: response.data.status,
          token: response.data.token,
          message: "Success",
        };
      } else {
        result = {
          success: response.data.status,
          message: response.data.message,
        };
      }
    })
    .catch((error) => {
      console.log(error);
      if (error.response.status === 500) {
        result = { success: false, message: "Server Error in Login user" };
      } else if (error.response.status === 404) {
        result = { success: false, message: "Server Down" };
      }
    });

  return result;
};

export const getCountriesOpenAQ = async () => {
  let result = {
    success: false,
    message: "Error fetching countries",
    data: [],
  };

  await axios
    .get(GET_COUNTRIES_API, {
      params: {
        limit: 200,
        page: 1,
        offset: 0,
        sort: "asc",
        orderBy: "name",
      },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
      },
    })
    .then((response) => {
      console.log(response);
      const results = response.data.results;
      let reducedResults = [];
      for (let i = 0; i < results.length; i++) {
        reducedResults.push({ code: results[i].code, name: results[i].name });
      }
      result = { success: true, message: "Success", data: reducedResults };
    })
    .catch((error) => {
      console.log(error);
      result = { success: false, message: "OpenAQ Server Down" };
    });

  return result;
};

export const getCitiesOpenAQ = async (country) => {
  let result = {
    success: false,
    message: "Error fetching cities",
    data: [],
  };

  await axios
    .get(GET_CITIES_API, {
      params: {
        limit: 20,
        page: 1,
        offset: 0,
        sort: "asc",
        orderBy: "city",
        countryCode: country.code,
      },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
      },
    })
    .then((response) => {
      const results = response.data.results;
      let reducedResults = [];
      for (let i = 0; i < results.length; i++) {
        if (!/\d/.test(results[i].city)) {
          reducedResults.push({
            country: results[i].country,
            city: results[i].city,
          });
        }
      }
      result = { success: true, message: "Success", data: reducedResults };
    })
    .catch((error) => {
      console.log(error);
      result = { success: false, message: "OpenAQ Server Down" };
    });

  return result;
};

export const getParametersOpenAQ = async (city) => {
  console.log(city);
  let result = {
    success: false,
    message: "Error fetching parameters",
    data: [],
  };

  await axios
    .get(GET_PARAMETERS_API, {
      params: {
        userId: getIdFromToken(),
        limit: 1,
        page: 1,
        offset: 0,
        sort: "desc",
        radius: 10,
        countryCode: city.country,
        city: city.city,
        orderBy: "city",
        dumpRaw: false,
      },
      headers: { Authorization: `Bearer ${localStorage.getItem("jwtToken")}` },
    })
    .then((response) => {
      result = { success: true, message: "Success", data: response.data };
    })
    .catch((error) => {
      console.log(error);
      result = { success: false, message: "OpenAQ Server Down" };
    });

  return result;
};

export const postSubscription = async (parameter, minutes) => {
  let result = {
    success: false,
    message: "Error creating subscription",
    data: [],
  };

  await axios
    .post(
      STRIPE_CREATE_SESSION_API,
      {
        userId: getIdFromToken(),
        locationId: parameter.locationId,
        parameterId: parameter.parameterId,
        cronSchedule: createCronSchedule(minutes),
        city: parameter.city,
        countryCode: parameter.countryCode,
        unit: parameter.unit,
        description: parameter.description,
        displayName: parameter.displayName,
      },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      }
    )
    .then((response) => {
      result = { success: true, message: "Success", data: response.data };
    })
    .catch((error) => {
      console.log(error);
      result = { success: false, message: "OpenAQ Server Down" };
    });

  return result;
};

export const unsubscribeOpenAQ = async (subscriptionId) => {
  let result = {
    success: false,
    message: "Error unsubscribing",
    data: [],
  };

  await axios
    .post(
      POST_UNSUBSCRIBE_API,
      {
        subscriptionId: subscriptionId,
      },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      }
    )
    .then((response) => {
      console.log(response);
      result = {
        success: true,
        message: "Success",
      };
    })
    .catch((error) => {
      console.log(error);
      result = { success: false, message: "OpenAQ Server Down" };
    });

  return result;
};
