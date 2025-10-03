package com.assignment.client_management.repositories;

import com.assignment.client_management.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientsRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByEmailIgnoreCase(String email);
}
