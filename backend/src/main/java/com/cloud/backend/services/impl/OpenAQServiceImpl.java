package com.cloud.backend.services.impl;

import com.cloud.backend.dto.ParameterResponse;
import com.cloud.backend.dto.openaq.*;
import com.cloud.backend.entities.SubscriptionEntity;
import com.cloud.backend.enumerations.SubscriptionStatus;
import com.cloud.backend.exceptions.OpenAQException;
import com.cloud.backend.services.OpenAQService;
import com.cloud.backend.services.SubscriptionService;
import com.cloud.backend.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class OpenAQServiceImpl implements OpenAQService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openAQ.api-key}")
    private String openAQApiKey;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public String getCountryResponse(Integer limit, Integer page, Integer offset, String sort, String orderBy) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constants.OPENAQ_API_HEADER, openAQApiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.GET_COUNTRIES_API)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .queryParam("offset", offset)
                .queryParam("sort", sort)
                .queryParam("order_by", orderBy);

        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new OpenAQException("Error in OpenAQ API Call");
        }
        return response.getBody();
    }

    @Override
    public String getCitiesResponse(Integer limit, Integer page, Integer offset, String sort, String country, String orderBy) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constants.OPENAQ_API_HEADER, openAQApiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.GET_CITIES_API)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .queryParam("offset", offset)
                .queryParam("sort", sort)
                .queryParam("country", country)
                .queryParam("order_by", orderBy);

        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new OpenAQException("Error in OpenAQ API Call");
        }
        return response.getBody();
    }

    @Override
    public List<ParameterResponse> getParametersResponse(Long userId, Integer limit, Integer page, Integer offset, String sort, Integer radius, String countryCode, String city, String orderBy, Boolean dumpRaw) {
        OpenAQParameterResponseDTO parametersFromOpenAQ = getParametersFromOpenAQ(limit, page, offset, sort, radius, countryCode, city, orderBy, dumpRaw);
        OpenAQParameterMetaResponseDTO parametersMeta = getParametersMeta();
        return frameResponse(userId, parametersFromOpenAQ, parametersMeta);
    }

    public List<ParameterResponse> frameResponse(Long userId, OpenAQParameterResponseDTO parametersFromOpenAQ, OpenAQParameterMetaResponseDTO parametersMeta) {
        List<ParameterResponse> responses = new ArrayList<>();
        if (parametersFromOpenAQ.getResults() == null || parametersFromOpenAQ.getResults().isEmpty()) {
            return responses;
        }

        Map<Long, OpenAQParameterMetaDTO> metaMap = new HashMap<>();
        for (OpenAQParameterMetaDTO parameterMeta : parametersMeta.getResults()) {
            metaMap.put(parameterMeta.getId(), parameterMeta);
        }
        OpenAQLocationResultsDTO resultDTO = parametersFromOpenAQ.getResults().get(0);
        for (OpenAQParameterDTO parameterDTO : resultDTO.getParameters()) {
            if (parameterDTO.getLastValue() < 0) {
                continue;
            }
            OpenAQParameterMetaDTO metaDTO = metaMap.get(parameterDTO.getParameterId());
            Optional<SubscriptionEntity> subscription = subscriptionService.getUserSubscriptionByLocationIdAndParameterIdAndSubscriptionStatus(userId, resultDTO.getId(), parameterDTO.getParameterId(), SubscriptionStatus.SUBSCRIBED);

            ParameterResponse parameterResponse = ParameterResponse.builder()
                    .parameterId(parameterDTO.getParameterId())
                    .unit(metaDTO.getPreferredUnit())
                    .cronSchedule(subscription.isEmpty() ? "" : subscription.get().getCronSchedule())
                    .description(metaDTO.getDescription())
                    .subscribed(subscription.isPresent())
                    .locationId(resultDTO.getId())
                    .subscriptionId(subscription.isPresent() ? subscription.get().getId() : null)
                    .city(resultDTO.getCity())
                    .countryCode(resultDTO.getCountry())
                    .value(parameterDTO.getLastValue())
                    .displayName(metaDTO.getDisplayName())
                    .build();

            responses.add(parameterResponse);
        }

        return responses;
    }

    private OpenAQParameterMetaResponseDTO getParametersMeta() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constants.OPENAQ_API_HEADER, openAQApiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.GET_PARAMETERS_META_API)
                .queryParam("limit", 100)
                .queryParam("page", 1)
                .queryParam("offset", 0)
                .queryParam("sort", "asc")
                .queryParam("order_by", "id");

        ResponseEntity<OpenAQParameterMetaResponseDTO> response = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), OpenAQParameterMetaResponseDTO.class);

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new OpenAQException("Error in OpenAQ API Call");
        }
        return response.getBody();
    }

    private OpenAQParameterResponseDTO getParametersFromOpenAQ(Integer limit, Integer page, Integer offset, String sort, Integer radius, String countryCode, String city, String orderBy, Boolean dumpRaw) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constants.OPENAQ_API_HEADER, openAQApiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.GET_PARAMETERS_API)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .queryParam("offset", offset)
                .queryParam("sort", sort)
                .queryParam("radius", radius)
                .queryParam("country", countryCode)
                .queryParam("city", city)
                .queryParam("order_by", orderBy)
                .queryParam("dump_raw", dumpRaw);

        ResponseEntity<OpenAQParameterResponseDTO> response = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), OpenAQParameterResponseDTO.class);

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new OpenAQException("Error in OpenAQ API Call");
        }
        return response.getBody();
    }
}
