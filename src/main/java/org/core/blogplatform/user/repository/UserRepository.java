package org.core.blogplatform.user.repository;

import org.core.blogplatform.user.domain.User;
import org.core.blogplatform.user.dto.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    //TODO redefine getByEmail method
    /*
    we have 2 methods with identical objective "getByEmail" with one return object and the other returns response entity
     */
    UserResponse getByEmail(String name);
}