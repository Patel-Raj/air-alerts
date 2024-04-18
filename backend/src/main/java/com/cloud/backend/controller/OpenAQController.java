package com.cloud.backend.controller;

import com.cloud.backend.dto.ParameterResponse;
import com.cloud.backend.exceptions.OpenAQException;
import com.cloud.backend.services.OpenAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("${cross.origin.allowed-origins}")
public class OpenAQController {

    @Autowired
    private OpenAQService openAQService;

    @GetMapping("/countries")
    public ResponseEntity<String> getCountries(@RequestParam Integer limit, @RequestParam Integer page, @RequestParam Integer offset, @RequestParam String sort,
                                               @RequestParam String orderBy) {
        try {
            String response = openAQService.getCountryResponse(limit, page, offset, sort, orderBy);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (OpenAQException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<String> getCities(@RequestParam Integer limit, @RequestParam Integer page, @RequestParam Integer offset, @RequestParam String sort,
                                            @RequestParam String countryCode, @RequestParam String orderBy) {
        try {
            String response = openAQService.getCitiesResponse(limit, page, offset, sort, countryCode, orderBy);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (OpenAQException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/parameters")
    public ResponseEntity<List<ParameterResponse>> getParameters(@RequestParam Long userId, @RequestParam Integer limit, @RequestParam Integer page,
                                                                 @RequestParam Integer offset, @RequestParam String sort,
                                                                 @RequestParam Integer radius, @RequestParam String countryCode,
                                                                 @RequestParam String city, @RequestParam String orderBy, @RequestParam Boolean dumpRaw) {
        try {
            List<ParameterResponse> response = openAQService.getParametersResponse(userId, limit, page, offset, sort, radius, countryCode, city, orderBy, dumpRaw);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (OpenAQException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
}
