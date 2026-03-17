package com.ecommerce.project.controller;

import com.ecommerce.project.payload.request.SellerApplicationRequest;
import com.ecommerce.project.payload.response.common.Success;
import com.ecommerce.project.payload.response.dto.SellerApplicationResponse;
import com.ecommerce.project.service.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Purpose
 * → Handles seller onboarding requests from authenticated users.
 */
@RestController
@RequestMapping("${api.base-path}/${api.version}/seller")
@RequiredArgsConstructor
@Slf4j
public class SellerController {

    private final SellerService sellerService;

    /**
     * Submit a seller application.
     * Requires ROLE_USER. Users who are already sellers are rejected at the service layer.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/apply")
    public ResponseEntity<Success<SellerApplicationResponse>> apply(
            @Valid @RequestBody SellerApplicationRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        log.info("Seller application received from user '{}'", principal.getUsername());
        SellerApplicationResponse response = sellerService.apply(request, principal.getUsername());
        return Success.of(HttpStatus.CREATED, "Seller application submitted successfully. Awaiting admin review.", response);
    }
}
