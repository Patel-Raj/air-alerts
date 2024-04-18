package com.cloud.backend.utils;

public class Constants {

    public static final String AUTH_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";


    public static final String MESSAGE_GROUP_ID = "MESSAGE_GROUP_ID";

    public static final String SUCCESS = "Success";

    public static final String VERIFY_EMAIL_MESSAGE = "VERIFY_EMAIL_MESSAGE";

    public static final String OPENAQ_API_HEADER = "X-API-Key";

    public static final String GET_COUNTRIES_API = "https://api.openaq.org/v2/countries";

    public static final String GET_CITIES_API = "https://api.openaq.org/v2/cities";

    public static final String GET_PARAMETERS_API = "https://api.openaq.org/v2/locations";

    public static final String GET_PARAMETERS_META_API = "https://api.openaq.org/v2/parameters";

    public static final String ALERT_TEMPLATE = "Current <PARAMETER> AQI in <CITY>, <COUNTRY> is <VALUE> <UNIT>";

    // Fetch it from DB when system has multiple offerings.
    public static final Long PRICE = 10L;

    public static final String STRIPE_INTENT_SUCCESS = "payment_intent.succeeded";
    public static final String STRIPE_INTENT_FAILED = "payment_intent.payment_failed";


}