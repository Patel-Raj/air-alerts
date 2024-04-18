import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import React, { useEffect, useState } from "react";
import CheckoutForm from "./CheckoutForm";
import { postSubscription } from "../../services/api";
import { useLocation } from "react-router-dom";

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

const CheckoutLanding = () => {
  const [clientSecret, setClientSecret] = useState("");
  let location = useLocation();
  const { parameter, minutes } = location.state;

  useEffect(() => {
    const fetchPaymentIntent = async () => {
      const result = await postSubscription(parameter, minutes);
      console.log(result);
      setClientSecret(result.data.clientSecret);
    };

    fetchPaymentIntent();
  }, []);

  const appearance = {
    theme: "stripe",
  };
  const options = {
    clientSecret,
    appearance,
  };

  return (
    <div className="App">
      {clientSecret && (
        <Elements options={options} stripe={stripePromise}>
          <CheckoutForm />
        </Elements>
      )}
    </div>
  );
};

export default CheckoutLanding;
