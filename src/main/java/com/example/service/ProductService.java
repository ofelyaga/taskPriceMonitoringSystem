package com.example.service;

import com.example.dto.ProductDTO;
import com.example.exception.AppException;
import com.example.exception.ProductAlreadyExistsException;
import com.example.exception.ProductNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        try {
            logger.info("Запрос на добавление продукта: {}", productDTO);
            Optional<Product> existingProduct = productRepository.findByNameAndManufacturerAndCategoryId(
                    productDTO.getName(),
                    productDTO.getManufacturer(),
                    productDTO.getCategoryId()
            );
            if (existingProduct.isPresent()) {
                logger.warn("Продукт с такими данными уже существует: {}", existingProduct.get());
                throw new ProductAlreadyExistsException(
                        productDTO.getName(),
                        productDTO.getManufacturer(),
                        productDTO.getCategoryId()
                );
            }
            Product product = ProductMapper.toEntity(productDTO);
            Product savedProduct = productRepository.save(product);
            logger.info("Продукт успешно добавлен: {}", savedProduct);
            return ProductMapper.toDTO(savedProduct);
        } catch (ProductAlreadyExistsException e) {
            logger.error("Ошибка при добавлении продукта(продкут уже существует): {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при добавлении продукта: {}", productDTO, e);
            throw new AppException("Ошибка при добавлении продукта(продкут уже существует)", e);
        }
    }

    public ProductDTO updateProduct(UUID productId, ProductDTO updatedProductDTO) {
        try {
            logger.info("Запрос на обновление продукта с ID {}: {}", productId, updatedProductDTO);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            ProductMapper.updateEntity(product, updatedProductDTO);
            Product updatedProduct = productRepository.save(product);
            logger.info("Продукт успешно обновлен: {}", updatedProduct);
            return ProductMapper.toDTO(updatedProduct);
        } catch (ProductNotFoundException e) {
            logger.error("Ошибка при обновлении продукта: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при обновлении продукта с ID {}: {}", productId, updatedProductDTO, e);
            throw new AppException("Ошибка при обновлении продукта", e);
        }
    }

    public void deleteProduct(UUID productId) {
        try {
            logger.info("Запрос на удаление продукта с ID: {}", productId);
            if (!productRepository.existsById(productId)) {
                throw new ProductNotFoundException(productId);
            }
            productRepository.deleteById(productId);
            logger.info("Продукт успешно удален с ID: {}", productId);
        } catch (ProductNotFoundException e) {
            logger.error("Ошибка при удалении продукта: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при удалении продукта с ID {}: {}", productId, e);
            throw new AppException("Ошибка при удалении продукта", e);
        }
    }

    public List<ProductDTO> getProductsByCategory(UUID categoryId) {
        try {
            logger.info("Запрос на получение продуктов по категории с ID: {}", categoryId);
            List<ProductDTO> products = productRepository.findByCategoryId(categoryId).stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно получены продукты по категории: количество = {}", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Ошибка при получении продуктов по категории с ID {}: {}", categoryId, e);
            throw new AppException("Ошибка при получении продуктов по категории", e);
        }
    }

    public List<ProductDTO> searchProductsByName(String name) {
        try {
            logger.info("Запрос на поиск продуктов по имени: {}", name);
            List<ProductDTO> products = productRepository.findByNameContainingIgnoreCase(name).stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно выполнено поиск продуктов по имени: количество = {}", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Ошибка при поиске продуктов по имени {}: {}", name, e);
            throw new AppException("Ошибка при поиске продуктов по имени", e);
        }
    }

    public List<ProductDTO> filterProductsByManufacturer(String manufacturer) {
        try {
            logger.info("Запрос на фильтрацию продуктов по производителю: {}", manufacturer);
            List<ProductDTO> products = productRepository.findByManufacturerContainingIgnoreCase(manufacturer).stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно выполнена фильтрация продуктов по производителю: количество = {}", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Ошибка при фильтрации продуктов по производителю {}: {}", manufacturer, e);
            throw new AppException("Ошибка при фильтрации продуктов по производителю", e);
        }
    }

    public List<ProductDTO> getAllProducts() {
        try {
            logger.info("Запрос на получение всех категорий");
            List<ProductDTO> products = productRepository.findAll().stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно получены все категории: количество = {}", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех категорий", e);
            throw new AppException("Ошибка при получении всех категорий", e);
        }
    }
}