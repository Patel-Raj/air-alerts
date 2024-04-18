const validateName = (name) => {
  if (name === "") {
    return { status: false, message: "Please enter valid Name!" };
  }

  const fullNameRegex = /^[a-zA-Z]{2,}$/;
  if (!fullNameRegex.test(name)) {
    return { status: false, message: "Please enter valid Name!" };
  }

  return { status: true, message: "" };
};

const validateEmail = (email) => {
  if (email === "") {
    return { status: false, message: "Please enter valid Email!" };
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    return { status: false, message: "Please enter valid Email!" };
  }

  return { status: true, message: "" };
};

const validatePassword = (password) => {
  if (password === "") {
    return { status: false, message: "Password cannot be empty!" };
  }

  return { status: true, message: "" };
};

const validateConfirmPassword = (password, confirmPassword) => {
  if (password !== confirmPassword) {
    return {
      status: false,
      message: "Password and Confirm Password did not match",
    };
  }

  return { status: true, message: "" };
};

export const validateSignupForm = (data) => {
  let result = validateName(data.name);
  if (!result.status) {
    return result;
  }

  result = validateEmail(data.email);
  if (!result.status) {
    return result;
  }

  result = validatePassword(data.password);
  if (!result.status) {
    return result;
  }

  result = validateConfirmPassword(data.password, data.confirmPassword);
  if (!result.status) {
    return result;
  }

  return result;
};
