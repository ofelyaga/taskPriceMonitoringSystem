package com.example.service;

import com.example.dto.ProductImportDTO;
import com.example.model.Price;
import com.example.model.Product;
import com.example.repository.CategoryRepository;
import com.example.repository.PriceRepository;
import com.example.repository.ProductRepository;
/*import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;*/
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final CategoryRepository categoryRepository;

    public void importProductsFromCsv(MultipartFile file) {
        /*try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Парсинг CSV в список объектов ProductImportDTO
            CsvToBean<ProductImportDTO> csvToBean = new CsvToBeanBuilder<ProductImportDTO>(reader)
                    .withType(ProductImportDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ProductImportDTO> productDTOs = csvToBean.parse();

            // Преобразование DTO в Entity и сохранение в базу данных
            List<Product> products = productDTOs.stream()
                    .map(dto -> {
                        Product product = new Product();
                        product.setName(dto.getName());
                        product.setManufacturer(dto.getManufacturer());
                        // Установка категории (нужно получить категорию из базы)
                        product.setCategory(categoryRepository.findById(dto.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("Category not found")));
                        return product;
                    })
                    .collect(Collectors.toList());

            productRepository.saveAll(products);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    public void importPricesFromJson(MultipartFile file) {
        // Логика парсинга JSON и добавления цен
        */
    }
}