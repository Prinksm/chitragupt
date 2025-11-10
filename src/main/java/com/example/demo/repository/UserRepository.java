package com.example.demo.repository;

import com.example.demo.entity.userEntity.AuthProviderType;
import com.example.demo.entity.userEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email) ;

    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
}
