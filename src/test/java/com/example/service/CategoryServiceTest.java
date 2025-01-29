package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.exception.AppException;
import com.example.exception.CategoryAlreadyExistsException;
import com.example.exception.CategoryNotFoundException;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void addCategory_WhenCategoryDoesNotExist_ShouldReturnSavedCategoryDTO() {
        CategoryDTO inputDTO = new CategoryDTO(null,"New Category");
        Category savedCategory = new Category(UUID.randomUUID(), "New Category");
        when(categoryRepository.findByName("New Category")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        CategoryDTO result = categoryService.addCategory(inputDTO);
        assertNotNull(result.getId());
        assertEquals("New Category", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void addCategory_WhenCategoryExists_ShouldThrowCategoryAlreadyExistsException() {
        CategoryDTO inputDTO = new CategoryDTO(null,"Existing Category");
        Category existing = new Category(UUID.randomUUID(), "Existing Category");
        when(categoryRepository.findByName("Existing Category")).thenReturn(Optional.of(existing));
        assertThrows(CategoryAlreadyExistsException.class,
            () -> categoryService.addCategory(inputDTO));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void addCategory_WhenRepositoryFails_ShouldThrowAppException() {
        CategoryDTO inputDTO = new CategoryDTO(null,"New");
        when(categoryRepository.findByName("New")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenThrow(new RuntimeException("DB Error"));
        assertThrows(AppException.class,
            () -> categoryService.addCategory(inputDTO));
    }

    @Test
    void updateCategory_WhenCategoryExists_ShouldUpdateAndReturnDTO() {
        UUID categoryId = UUID.randomUUID();
        Category existing = new Category(categoryId, "Old Name");
        CategoryDTO updatedDTO = new CategoryDTO(null, "New Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);
        CategoryDTO result = categoryService.updateCategory(categoryId, updatedDTO);
        assertEquals("New Name", result.getName());
        verify(categoryRepository).save(existing);
    }

    @Test
    void updateCategory_WhenCategoryNotFound_ShouldThrowCategoryNotFoundException() {
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class,
            () -> categoryService.updateCategory(categoryId, new CategoryDTO(null, "Name")));
    }

    @Test
    void deleteCategory_WhenCategoryExists_ShouldDeleteSuccessfully() {
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        categoryService.deleteCategory(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void deleteCategory_WhenCategoryNotFound_ShouldThrowCategoryNotFoundException() {
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        assertThrows(CategoryNotFoundException.class,
            () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, never()).deleteById(categoryId);
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategoryDTOs() {
        // Arrange
        List<Category> categories = List.of(
            new Category(UUID.randomUUID(), "Cat1"),
            new Category(UUID.randomUUID(), "Cat2")
        );
        when(categoryRepository.findAll()).thenReturn(categories);
        List<CategoryDTO> result = categoryService.getAllCategories();
        assertEquals(2, result.size());
        assertEquals("Cat1", result.get(0).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void getAllCategories_WhenEmpty_ShouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        List<CategoryDTO> result = categoryService.getAllCategories();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCategories_WhenRepositoryFails_ShouldThrowAppException() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("DB Error"));
        assertThrows(AppException.class, () -> categoryService.getAllCategories());
    }
}