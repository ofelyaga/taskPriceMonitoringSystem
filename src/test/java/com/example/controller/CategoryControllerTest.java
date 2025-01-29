package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.exception.CategoryAlreadyExistsException;
import com.example.exception.CategoryNotFoundException;
import com.example.exception.GlobalExceptionHandler;
import com.example.service.CategoryService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final UUID testCategoryId = UUID.randomUUID();
    private final CategoryDTO testCategoryDTO = new CategoryDTO(testCategoryId, "Electronics");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void addCategory_ShouldReturnCreatedCategory() throws Exception {
        when(categoryService.addCategory(any(CategoryDTO.class))).thenReturn(testCategoryDTO);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testCategoryId.toString()))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        when(categoryService.updateCategory(eq(testCategoryId), any(CategoryDTO.class)))
                .thenReturn(testCategoryDTO);

        mockMvc.perform(put("/api/categories/{categoryId}", testCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void updateCategory_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(categoryService.updateCategory(eq(testCategoryId), any(CategoryDTO.class)))
                .thenThrow(new CategoryNotFoundException(testCategoryId));

        mockMvc.perform(put("/api/categories/{categoryId}", testCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(testCategoryId);

        mockMvc.perform(delete("/api/categories/{categoryId}", testCategoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_WithInvalidUUID_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/categories/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategories_ShouldReturnCategoryList() throws Exception {
        List<CategoryDTO> categories = List.of(testCategoryDTO);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void addCategory_WhenAlreadyExists_ShouldReturnConflict() throws Exception {
        when(categoryService.addCategory(any(CategoryDTO.class)))
                .thenThrow(new CategoryAlreadyExistsException("Electronics"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isConflict());
    }
}