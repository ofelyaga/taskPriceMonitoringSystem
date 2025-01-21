package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, name = "id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manufacturer; // Производитель

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Категория товара
}