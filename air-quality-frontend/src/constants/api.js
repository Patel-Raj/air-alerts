const BACKEND_BASE_URL = process.env.REACT_APP_BACKEND_BASE_URL;

export const REGISTER_API = BACKEND_BASE_URL + "/rest/auth/register";
export const VERIFY_EMAIL_API = BACKEND_BASE_URL + "/rest/auth/verify-email";
export const LOGIN_API = BACKEND_BASE_URL + "/rest/auth/login";
export const GET_COUNTRIES_API = BACKEND_BASE_URL + "/countries";
export const GET_CITIES_API = BACKEND_BASE_URL + "/cities";
export const GET_PARAMETERS_API = BACKEND_BASE_URL + "/parameters";
export const POST_UNSUBSCRIBE_API = BACKEND_BASE_URL + "/unsubscribe";
export const STRIPE_CREATE_SESSION_API =
  BACKEND_BASE_URL + "/create-payment-intent";
