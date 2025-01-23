package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.exception.AppException;
import com.example.exception.CategoryAlreadyExistsException;
import com.example.exception.CategoryNotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        try {
            logger.info("Запрос на добавление категории: {}", categoryDTO);
            Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
            if (existingCategory.isPresent()) {
                logger.warn("Категория с таким именем уже существует: {}", existingCategory.get());
                throw new CategoryAlreadyExistsException(categoryDTO.getName());
            }
            Category category = CategoryMapper.toEntity(categoryDTO);
            Category savedCategory = categoryRepository.save(category);
            logger.info("Категория успешно добавлена: {}", savedCategory);
            return CategoryMapper.toDTO(savedCategory);
        } catch (CategoryAlreadyExistsException e) {
            logger.error("Ошибка при добавлении категории: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при добавлении категории: {}", categoryDTO, e);
            throw new AppException("Ошибка при добавлении категории", e);
        }
    }

    public CategoryDTO updateCategory(UUID categoryId, CategoryDTO updatedCategoryDTO) {
        try {
            logger.info("Запрос на обновление категории с ID {}: {}", categoryId, updatedCategoryDTO);
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));
            CategoryMapper.updateEntity(category, updatedCategoryDTO);
            Category updatedCategory = categoryRepository.save(category);
            logger.info("Категория успешно обновлена: {}", updatedCategory);
            return CategoryMapper.toDTO(updatedCategory);
        } catch (CategoryNotFoundException e) {
            logger.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при обновлении категории с ID {}: {}", categoryId, updatedCategoryDTO, e);
            throw new AppException("Ошибка при обновлении категории", e);
        }
    }

    public void deleteCategory(UUID categoryId) {
        try {
            logger.info("Запрос на удаление категории с ID: {}", categoryId);
            if (!categoryRepository.existsById(categoryId)) {
                throw new CategoryNotFoundException(categoryId);
            }
            categoryRepository.deleteById(categoryId);
            logger.info("Категория успешно удалена с ID: {}", categoryId);
        } catch (CategoryNotFoundException e) {
            logger.error("Ошибка при удалении категории: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при удалении категории с ID {}: {}", categoryId, e);
            throw new AppException("Ошибка при удалении категории", e);
        }
    }

    public List<CategoryDTO> getAllCategories() {
        try {
            logger.info("Запрос на получение всех категорий");
            List<CategoryDTO> categories = categoryRepository.findAll().stream()
                    .map(CategoryMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно получены все категории: количество = {}", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех категорий", e);
            throw new AppException("Ошибка при получении всех категорий", e);
        }
    }
}