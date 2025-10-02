package com.assignment.client_management.repositories;

import com.assignment.client_management.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<ClientEntity, Long> {
}
