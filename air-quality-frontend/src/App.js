import React from "react";
import { Route, Routes } from "react-router-dom";
import Landing from "./pages/Landing";
import VerifyEmail from "./components/auth/VerifyEmail";
import Header from "./components/header/Header";
import Search from "./components/search/Search";
import { AuthProvider } from "./utils/auth";
import CheckoutLanding from "./components/payment/CheckoutLanding";
import PaymentSuccess from "./components/payment/PaymentSuccess";

function App() {
  return (
    <AuthProvider>
      <Header />
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="home" element={<Search />} />
        <Route path="verify-email/:id" element={<VerifyEmail />} />
        <Route path="checkout" element={<CheckoutLanding />} />
        <Route path="payment-success" element={<PaymentSuccess />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
