package com.ecommerce.project.service;

import com.ecommerce.project.exception.custom.ApiException;
import com.ecommerce.project.exception.custom.ResourceNotFoundException;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.SellerApplication;
import com.ecommerce.project.model.User;
import com.ecommerce.project.model.enums.ApplicationStatus;
import com.ecommerce.project.model.enums.Roles;
import com.ecommerce.project.payload.request.RejectionRequest;
import com.ecommerce.project.payload.request.SellerApplicationRequest;
import com.ecommerce.project.payload.response.dto.SellerApplicationResponse;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.SellerApplicationRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.service.interfaces.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final SellerApplicationRepository sellerApplicationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public SellerApplicationResponse apply(SellerApplicationRequest request, String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        boolean alreadySeller = user.getRoles().stream()
                .anyMatch(role -> role.getRoles() == Roles.ROLE_SELLER);
        if (alreadySeller) {
            throw new ApiException("You are already a seller");
        }

        boolean hasPendingApplication = sellerApplicationRepository.existsByUserAndStatus(user, ApplicationStatus.PENDING);
        if (hasPendingApplication) {
            throw new ApiException("You already have a pending seller application");
        }

        SellerApplication application = new SellerApplication();
        application.setUser(user);
        application.setBusinessName(request.getBusinessName());
        application.setDescription(request.getDescription());

        SellerApplication saved = sellerApplicationRepository.save(application);
        log.info("Seller application submitted by user '{}' with business name '{}'", username, request.getBusinessName());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerApplicationResponse> getPendingApplications() {
        return sellerApplicationRepository.findByStatusOrderByCreatedAtAsc(ApplicationStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SellerApplicationResponse approve(Long applicationId) {
        SellerApplication application = findApplicationOrThrow(applicationId);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ApiException("Application %d is already %s".formatted(applicationId, application.getStatus()));
        }

        Role sellerRole = roleRepository.findByRoles(Roles.ROLE_SELLER)
                .orElseThrow(() -> new IllegalStateException("ROLE_SELLER not found in database"));

        User user = application.getUser();
        user.getRoles().add(sellerRole);
        userRepository.save(user);

        application.setStatus(ApplicationStatus.APPROVED);
        application.setReviewedAt(LocalDateTime.now());
        sellerApplicationRepository.save(application);

        log.info("Seller application {} approved. User '{}' granted ROLE_SELLER", applicationId, user.getName());

        return toResponse(application);
    }

    @Override
    @Transactional
    public SellerApplicationResponse reject(Long applicationId, RejectionRequest rejectionRequest) {
        SellerApplication application = findApplicationOrThrow(applicationId);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ApiException("Application %d is already %s".formatted(applicationId, application.getStatus()));
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setReviewedAt(LocalDateTime.now());
        application.setRejectionReason(rejectionRequest.getReason());
        sellerApplicationRepository.save(application);

        log.info("Seller application {} rejected. Reason: {}", applicationId, rejectionRequest.getReason());

        return toResponse(application);
    }

    private SellerApplication findApplicationOrThrow(Long applicationId) {
        return sellerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("SellerApplication", "id", applicationId));
    }

    private SellerApplicationResponse toResponse(SellerApplication app) {
        return new SellerApplicationResponse(
                app.getId(),
                app.getUser().getId(),
                app.getUser().getName(),
                app.getBusinessName(),
                app.getDescription(),
                app.getStatus(),
                app.getCreatedAt(),
                app.getReviewedAt(),
                app.getRejectionReason()
        );
    }
}
