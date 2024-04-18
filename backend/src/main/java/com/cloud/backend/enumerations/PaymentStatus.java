package com.cloud.backend.enumerations;

public enum PaymentStatus {
    CREATED("payment_intent.created"),
    FAILED("payment_intent.failed"),
    SUCCEEDED("payment_intent.succeeded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
