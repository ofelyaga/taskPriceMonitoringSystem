package com.example.repository;

import com.example.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findByCity(String city);
    Optional<Store> findByName(String name);
    boolean existsByName(String name);
}