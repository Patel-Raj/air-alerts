package com.cloud.backend.services;

import com.cloud.backend.dto.ParameterResponse;

import java.util.List;

public interface OpenAQService {
    String getCountryResponse(Integer limit, Integer page, Integer offset, String sort, String orderBy);

    String getCitiesResponse(Integer limit, Integer page, Integer offset, String sort, String country, String orderBy);

    List<ParameterResponse> getParametersResponse(Long userId, Integer limit, Integer page, Integer offset, String sort, Integer radius, String countryCode, String city, String orderBy, Boolean dumpRaw);
}
