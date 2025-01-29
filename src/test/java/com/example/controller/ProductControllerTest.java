package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.exception.GlobalExceptionHandler;
import com.example.exception.ProductNotFoundException;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void addProduct_ShouldReturnCreatedProduct() throws Exception {
        ProductDTO inputDTO = createTestProductDTO();
        ProductDTO savedDTO = createTestProductDTO();
        savedDTO.setId(UUID.randomUUID());

        when(productService.addProduct(any(ProductDTO.class))).thenReturn(savedDTO);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(inputDTO.getName()));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO updatedDTO = createTestProductDTO();
        updatedDTO.setName("Updated Product");

        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        UUID productId = UUID.randomUUID();
        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/products/{productId}", productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsList() throws Exception {
        UUID categoryId = UUID.randomUUID();
        List<ProductDTO> products = Arrays.asList(
                createTestProductDTO(),
                createTestProductDTO()
        );

        when(productService.getProductsByCategory(categoryId)).thenReturn(products);

        mockMvc.perform(get("/api/products/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void searchProductsByName_ShouldReturnFilteredList() throws Exception {
        String searchName = "test";
        ProductDTO testProduct = createTestProductDTO();
        testProduct.setName("test product");

        when(productService.searchProductsByName(searchName)).thenReturn(List.of(testProduct));

        mockMvc.perform(get("/api/products/search")
                        .param("name", searchName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("test product")); // Проверяем полное имя
    }

    @Test
    void filterProductsByManufacturer_ShouldReturnFilteredList() throws Exception {
        String manufacturer = "TestCo";
        List<ProductDTO> products = Arrays.asList(createTestProductDTO());

        when(productService.filterProductsByManufacturer(manufacturer)).thenReturn(products);

        mockMvc.perform(get("/api/products/filter")
                .param("manufacturer", manufacturer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].manufacturer").value(manufacturer));
    }

    @Test
    void updateProduct_WhenProductNotFound_ShouldThrowException() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO updatedDTO = createTestProductDTO();

        when(productService.updateProduct(eq(productId), any(ProductDTO.class)))
                .thenThrow(new ProductNotFoundException(productId));

        mockMvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WithInvalidUUID_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/products/{productId}", "invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid UUID format")));
    }

    private ProductDTO createTestProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Test Product");
        dto.setManufacturer("TestCo");
        dto.setCategoryId(UUID.randomUUID());
        return dto;
    }
}