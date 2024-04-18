import {
  AppBar,
  Box,
  Button,
  IconButton,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
} from "@mui/material";
import React from "react";
import Logo from "../../assets/images/logo.png";
import { AccountCircle } from "@mui/icons-material";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../utils/auth";

const pages = [{ content: "Home", to: "/home" }];

function Header() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const navigate = useNavigate();
  const auth = useAuth();

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const navStyle = ({ isActive }) => {
    return {
      fontWeight: isActive ? "bold" : "normal",
      textDecoration: "none",
      border: isActive ? "1px solid rgb(0,123,250)" : "none",
    };
  };

  const handleLogout = () => {
    auth.logout();
    handleClose();
    navigate("/");
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" sx={{ backgroundColor: "white" }}>
        <Toolbar>
          <img src={Logo} alt="logo"></img>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, marginLeft: "30px", color: "#007bff" }}
          >
            Air Quality Alerts
          </Typography>
          {auth.isUserLoggedIn() && (
            <Box
              sx={{
                flexGrow: 1,
                display: {
                  md: "flex",
                  marginLeft: "1400px",
                  "& > *:not(:last-child)": { marginRight: "30px" },
                },
              }}
            >
              {pages.map((page) => (
                <NavLink key={page.content} to={page.to} style={navStyle}>
                  <Button
                    sx={{
                      display: "block",
                      width: "100%",
                      textAlign: "center",
                    }}
                  >
                    {page.content}
                  </Button>
                </NavLink>
              ))}
            </Box>
          )}

          {auth.isUserLoggedIn() && (
            <div>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleMenu}
                color="inherit"
              >
                <AccountCircle sx={{ color: "#007bff" }} />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                keepMounted
                transformOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
              >
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
              </Menu>
            </div>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
}

export default Header;
