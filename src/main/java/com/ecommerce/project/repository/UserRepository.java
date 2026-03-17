package com.ecommerce.project.repository;

import com.ecommerce.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Purpose
 * → Fetch user during authentication.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByName(String name);

}
