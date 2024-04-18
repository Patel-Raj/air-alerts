import { Link } from "react-router-dom";
import React from "react";

const PaymentSuccess = () => {
  return (
    <div className="payment-success-container">
      <h1 className="payment-success-heading">Payment Successful!</h1>
      <p className="payment-success-message">
        Your payment was successful. Thank you for your subscription!
      </p>
      <Link to="/home">
        <button className="home-button">Home</button>
      </Link>
    </div>
  );
};

export default PaymentSuccess;
