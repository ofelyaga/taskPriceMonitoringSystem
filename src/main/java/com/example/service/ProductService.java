package com.example.service;

import com.example.dto.ProductDTO;
import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO updateProduct(UUID productId, ProductDTO updatedProductDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.updateEntity(updatedProductDTO, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }

    public List<ProductDTO> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> filterProductsByManufacturer(String manufacturer) {
        return productRepository.findByManufacturerContainingIgnoreCase(manufacturer).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

}