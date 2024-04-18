import { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [jwtToken, setJwtToken] = useState(null);

  useEffect(() => {
    const jwtToken = localStorage.getItem("jwtToken");
    if (jwtToken) {
      setJwtToken(jwtToken);
    }
  }, []);

  const login = (token) => {
    console.log("callled");
    localStorage.setItem("jwtToken", token);
    setJwtToken(token);
  };

  const logout = () => {
    localStorage.removeItem("jwtToken");
    setJwtToken(null);
  };

  const isUserLoggedIn = () => {
    return jwtToken !== null;
  };

  return (
    <AuthContext.Provider value={{ login, logout, isUserLoggedIn }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
