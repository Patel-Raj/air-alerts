package org.cloud.openaq;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloud.model.OpenAqResponse;
import org.cloud.model.Parameter;
import org.cloud.model.Response;
import org.cloud.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Api {

    public static Response fetchMeasurement(Long locationId, LambdaLogger logger) throws IOException {
        Map<String, String> envVariables = System.getenv();
        URL url = new URL( Constants.API.replace("##", String.valueOf(locationId)));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-API-Key", envVariables.get("OPENAQ_API_KEY"));
        int responseCode = connection.getResponseCode();
        logger.log("openAPI response code: " + responseCode);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseString.append(line);
        }
        reader.close();

        ObjectMapper objectMapper = new ObjectMapper();
        OpenAqResponse response = objectMapper.readValue(responseString.toString(), OpenAqResponse.class);
        logger.log("openAPI response: " + response);

        return Response.builder()
                .locationId(locationId)
                .parameters(response.getResults().isEmpty() ? new ArrayList<>() : response.getResults().get(0).getParameters())
                .build();
    }

    public static String getResponse(Map<String, String> event, LambdaLogger logger) throws IOException {
        Long locationId = Long.valueOf(event.get("locationId"));
        Long parameterId = Long.valueOf(event.get("parameterId"));
        String template = event.get("template");

        Response response = Api.fetchMeasurement(locationId, logger);
        for (Parameter parameter : response.getParameters()) {
            if (Objects.equals(parameter.getId(), parameterId)) {
                return template.replace("<VALUE>", Double.toString(parameter.getLastValue()));
            }
        }

        return "No Data Found";
    }
}
