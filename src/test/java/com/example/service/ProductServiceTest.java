package com.example.service;

import com.example.dto.ProductDTO;
import com.example.exception.AppException;
import com.example.exception.ProductAlreadyExistsException;
import com.example.exception.ProductNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.model.Category;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductDTO createProductDTO(String name, String manufacturer, UUID categoryId) {
        ProductDTO dto = new ProductDTO();
        dto.setName(name);
        dto.setManufacturer(manufacturer);
        dto.setCategoryId(categoryId);
        return dto;
    }

    private Product createProductEntity(UUID id, String name, String manufacturer, Category category) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setManufacturer(manufacturer);
        product.setCategory(category);
        return product;
    }
    private Category createCategory(UUID id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    @Test
    void addProduct_ShouldSaveNewProduct() {
        UUID categoryId = UUID.randomUUID();
        Category category = createCategory(categoryId, "Electronics");
        ProductDTO inputDTO = createProductDTO("Laptop", "Dell", categoryId);
        Product savedProduct = createProductEntity(UUID.randomUUID(), "Laptop", "Dell", category);

        when(productRepository.findByNameAndManufacturerAndCategoryId(
                "Laptop", "Dell", categoryId))
                .thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.addProduct(inputDTO);

        assertNotNull(result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(categoryId, result.getCategoryId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void addProduct_ShouldThrowWhenProductExists() {
        UUID categoryId = UUID.randomUUID();
        Category category = createCategory(categoryId, "Electronics");
        ProductDTO inputDTO = createProductDTO("Phone", "Apple", categoryId);
        Product existing = createProductEntity(UUID.randomUUID(), "Phone", "Apple", category);

        when(productRepository.findByNameAndManufacturerAndCategoryId(
                "Phone", "Apple", categoryId))
                .thenReturn(Optional.of(existing));

        assertThrows(ProductAlreadyExistsException.class,
                () -> productService.addProduct(inputDTO));
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_ShouldUpdateExistingProduct() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Category category = createCategory(categoryId, "Electronics");
        Product existing = createProductEntity(productId, "Old", "OldMan", category);
        ProductDTO updateDTO = createProductDTO("New", "NewMan", categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);

        ProductDTO result = productService.updateProduct(productId, updateDTO);

        assertEquals("New", result.getName());
        assertEquals("NewMan", result.getManufacturer());
        assertEquals(categoryId, result.getCategoryId());
        verify(productRepository).save(existing);
    }

    @Test
    void updateProduct_ShouldThrowWhenProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
            () -> productService.updateProduct(productId, new ProductDTO()));
    }

    @Test
    void deleteProduct_ShouldDeleteWhenExists() {
        UUID productId = UUID.randomUUID();
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        UUID categoryId = UUID.randomUUID();
        Category category = createCategory(categoryId, "Electronics");
        List<Product> products = List.of(
                createProductEntity(UUID.randomUUID(), "P1", "M1", category),
                createProductEntity(UUID.randomUUID(), "P2", "M2", category)
        );
        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);

        List<ProductDTO> result = productService.getProductsByCategory(categoryId);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getCategoryId().equals(categoryId)));
    }

    @Test
    void searchProductsByName_ShouldReturnMatchingProducts() {
        String searchTerm = "pro";
        Category category = createCategory(UUID.randomUUID(), "Electronics");
        List<Product> products = List.of(
                createProductEntity(UUID.randomUUID(), "Product 1", "M1", category),
                createProductEntity(UUID.randomUUID(), "PRODUCT 2", "M2", category)
        );
        when(productRepository.findByNameContainingIgnoreCase(searchTerm)).thenReturn(products);

        List<ProductDTO> result = productService.searchProductsByName(searchTerm);

        assertEquals(2, result.size());
        verify(productRepository).findByNameContainingIgnoreCase(searchTerm);
    }

    @Test
    void filterProductsByManufacturer_ShouldReturnFilteredProducts() {
        String manufacturer = "tech";
        Category category = createCategory(UUID.randomUUID(), "Electronics");
        List<Product> products = List.of(
                createProductEntity(UUID.randomUUID(), "P1", "TechCorp", category),
                createProductEntity(UUID.randomUUID(), "P2", "MyTech", category)
        );
        when(productRepository.findByManufacturerContainingIgnoreCase(manufacturer)).thenReturn(products);

        List<ProductDTO> result = productService.filterProductsByManufacturer(manufacturer);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(dto -> dto.getManufacturer().toLowerCase().contains(manufacturer.toLowerCase())));
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        Category category = createCategory(UUID.randomUUID(), "Electronics");
        List<Product> products = List.of(
                createProductEntity(UUID.randomUUID(), "P1", "M1", category),
                createProductEntity(UUID.randomUUID(), "P2", "M2", category)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void deleteProduct_ShouldThrowWhenProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class,
            () -> productService.deleteProduct(productId));
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    void addProduct_ShouldThrowAppExceptionOnError() {
        ProductDTO inputDTO = createProductDTO("Test", "TestMan", UUID.randomUUID());
        when(productRepository.findByNameAndManufacturerAndCategoryId(any(), any(), any()))
            .thenThrow(new RuntimeException("DB Error"));

        assertThrows(AppException.class,
            () -> productService.addProduct(inputDTO));
    }
}