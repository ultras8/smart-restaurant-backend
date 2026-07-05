package com.personProject.smartRestaurant.users;

import com.personProject.smartRestaurant.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    //หาด้วยเมลหรือusername
    Optional<User> findByUsernameOrEmail(String username, String email);
}