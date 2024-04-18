import { jwtDecode } from "jwt-decode";

export const createCronSchedule = (minutes) => {
  return `cron(0/${minutes} * * * ? *)`;
};

export const getIdFromToken = () => {
  return jwtDecode(localStorage.getItem("jwtToken")).id;
};
