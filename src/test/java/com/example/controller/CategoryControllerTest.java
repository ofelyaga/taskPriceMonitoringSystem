package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.exception.CategoryAlreadyExistsException;
import com.example.exception.CategoryNotFoundException;
import com.example.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final UUID testId = UUID.randomUUID();
    private final CategoryDTO testCategory = new CategoryDTO(testId, "Electronics");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void addCategory_ShouldReturnCreated() throws Exception {
        when(categoryService.addCategory(any())).thenReturn(testCategory);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void addCategory_ShouldHandleConflict() throws Exception {
        when(categoryService.addCategory(any()))
                .thenThrow(new CategoryAlreadyExistsException("Electronics"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateCategory_ShouldReturnOk() throws Exception {
        when(categoryService.updateCategory(eq(testId), any()))
                .thenReturn(testCategory);

        mockMvc.perform(put("/api/categories/{categoryId}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()));
    }

    @Test
    void updateCategory_ShouldHandleNotFound() throws Exception {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        CategoryDTO updateDTO = new CategoryDTO(UUID.randomUUID(), "New description");

        when(categoryService.updateCategory(eq(categoryId), any(CategoryDTO.class)))
                .thenThrow(new CategoryNotFoundException(categoryId));

        // Act & Assert
        mockMvc.perform(put("/api/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Категория с ID " + categoryId + " не найдена"));
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/categories/{categoryId}", testId))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(testId);
    }

    @Test
    void deleteCategory_ShouldHandleInvalidUUID() throws Exception {
        mockMvc.perform(delete("/api/categories/{categoryId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        List<CategoryDTO> categories = List.of(testCategory);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}

// Обработчик исключений для контроллера
class ControllerExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<String> handleNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<String> handleConflict(CategoryAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}