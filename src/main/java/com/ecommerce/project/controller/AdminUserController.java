package com.ecommerce.project.controller;

import com.ecommerce.project.payload.request.RejectionRequest;
import com.ecommerce.project.payload.response.common.Success;
import com.ecommerce.project.payload.response.dto.SellerApplicationResponse;
import com.ecommerce.project.service.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Purpose
 * → Admin operations for managing users and seller applications.
 */
@RestController
@RequestMapping("${api.base-path}/${api.version}/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final SellerService sellerService;

    /**
     * List all pending seller applications ordered by submission date (oldest first).
     */
    @GetMapping("/seller-applications")
    public ResponseEntity<Success<List<SellerApplicationResponse>>> getPendingApplications() {
        List<SellerApplicationResponse> applications = sellerService.getPendingApplications();
        String message = applications.isEmpty()
                ? "No pending seller applications"
                : "%d pending seller application(s) found".formatted(applications.size());
        return Success.of(HttpStatus.OK, message, applications);
    }

    /**
     * Approve a pending seller application. Grants ROLE_SELLER to the applicant.
     */
    @PostMapping("/seller-applications/{id}/approve")
    public ResponseEntity<Success<SellerApplicationResponse>> approve(@PathVariable Long id) {
        log.info("Admin approving seller application id={}", id);
        SellerApplicationResponse response = sellerService.approve(id);
        return Success.of(HttpStatus.OK, "Seller application approved. User has been granted seller privileges.", response);
    }

    /**
     * Reject a pending seller application with a mandatory reason.
     */
    @PostMapping("/seller-applications/{id}/reject")
    public ResponseEntity<Success<SellerApplicationResponse>> reject(
            @PathVariable Long id,
            @Valid @RequestBody RejectionRequest rejectionRequest) {

        log.info("Admin rejecting seller application id={}", id);
        SellerApplicationResponse response = sellerService.reject(id, rejectionRequest);
        return Success.of(HttpStatus.OK, "Seller application rejected.", response);
    }
}
