package com.cloud.backend.controller;

import com.cloud.backend.dto.UnsubscribeRequest;
import com.cloud.backend.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@CrossOrigin("${cross.origin.allowed-origins}")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/unsubscribe")
    public ResponseEntity<Long> subscription(@RequestBody UnsubscribeRequest request) {
        subscriptionService.unsubscribe(request);
        return ResponseEntity.status(HttpStatus.OK).body(request.getSubscriptionId());
    }
}