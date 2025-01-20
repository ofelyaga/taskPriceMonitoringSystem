package com.example.repository;

import com.example.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRepository extends JpaRepository<Price, UUID> {
    @Query("SELECT p FROM Price p WHERE p.product.id = :productId AND p.store.id = :storeId AND p.date BETWEEN :startDate AND :endDate ORDER BY p.date DESC")
    List<Price> findByProductIdAndStoreIdAndDateBetween(
            @Param("productId") UUID productId,
            @Param("storeId") UUID storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT p FROM Price p WHERE p.product.id = :productId AND p.store.id IN :storeIds ORDER BY p.date DESC")
    List<Price> findByProductIdAndStoreIdIn(
            @Param("productId") UUID productId,
            @Param("storeIds") List<String> storeIds
    );

    Price findFirstByProductIdAndStoreIdOrderByDateDesc(UUID productId, UUID storeId);
}