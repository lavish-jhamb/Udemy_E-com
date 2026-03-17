package com.ecommerce.project.repository;

import com.ecommerce.project.model.SellerApplication;
import com.ecommerce.project.model.User;
import com.ecommerce.project.model.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerApplicationRepository extends JpaRepository<SellerApplication, Long> {

    boolean existsByUserAndStatus(User user, ApplicationStatus status);

    List<SellerApplication> findByStatusOrderByCreatedAtAsc(ApplicationStatus status);
}
