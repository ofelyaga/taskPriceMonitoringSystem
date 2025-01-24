package com.example.utility;

import com.example.dto.*;
import com.example.mapper.*;
import com.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CsvUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);


    public List<Product> importProducts(InputStream stream) throws IOException {
        List<Product> importedProducts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(UUID.fromString(fields[0]));
                productDTO.setName(fields[1]);
                productDTO.setManufacturer(fields[2]);
                productDTO.setCategoryId(UUID.fromString(fields[3]));

                Product product = ProductMapper.toEntity(productDTO);
                importedProducts.add(product);
            }
        } catch (IOException e) {
            logger.error("Ошибка при импорте продуктов: {}", e.getMessage());
            throw e;
        }
        return importedProducts;
    }

    public byte[] productsToCsvBytes(List<ProductDTO> products) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (ProductDTO product : products) {
                writer.write(productToCsv(product));
                writer.newLine();
            }
        }
        return outputStream.toByteArray();
    }

    private String productToCsv(ProductDTO productDTO) {
        return productDTO.getId() + "," + productDTO.getName() + "," + productDTO.getManufacturer() + "," + productDTO.getCategoryId();
    }

    public List<Price> importPrices(String filePath) throws IOException {
        List<Price> importedPrices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                PriceDTO priceDTO = new PriceDTO();
                priceDTO.setId(UUID.fromString(fields[0]));
                priceDTO.setProductId(UUID.fromString(fields[1]));
                priceDTO.setStoreId(UUID.fromString(fields[2]));
                priceDTO.setValue(Double.parseDouble(fields[3]));
                priceDTO.setDate(LocalDate.parse(fields[4], DateTimeFormatter.ofPattern("yyyy-MMM-dd")));

                Price price = PriceMapper.toEntity(priceDTO);
                importedPrices.add(price);
            }
        } catch (IOException e) {
            logger.error("Ошибка при импорте цен: {}", e.getMessage());
            throw e;
        }
        return importedPrices;
    }

    public byte[] pricesToCsvBytes(List<PriceDTO> prices) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (PriceDTO price : prices) {
                writer.write(priceToCsv(price));
                writer.newLine();
            }
        }
        return outputStream.toByteArray();
    }

    private String priceToCsv(PriceDTO priceDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return priceDTO.getId() + "," + priceDTO.getProductId() + "," + priceDTO.getStoreId() + "," +
                priceDTO.getValue() + "," + dateFormat.format(priceDTO.getDate());
    }

    public List<Store> importStores(String filePath) throws IOException {
        List<Store> importedStores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                StoreDTO storeDTO = new StoreDTO();
                storeDTO.setId(UUID.fromString(fields[0]));
                storeDTO.setName(fields[1]);
                storeDTO.setCity(fields[2]);

                Store store = StoreMapper.toEntity(storeDTO);
                importedStores.add(store);
            }
        } catch (IOException e) {
            logger.error("Ошибка при импорте магазинов: {}", e.getMessage());
            throw e;
        }
        return importedStores;
    }

    public byte[] storesToCsvBytes(List<StoreDTO> stores) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (StoreDTO store : stores) {
                writer.write(storeToCsv(store));
                writer.newLine();
            }
        }
        return outputStream.toByteArray();
    }

    private String storeToCsv(StoreDTO storeDTO) {
        return storeDTO.getId() + "," + storeDTO.getName() + "," + storeDTO.getCity();
    }

    public List<Category> importCategories(String filePath) throws IOException {
        List<Category> importedCategories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(UUID.fromString(fields[0]));
                categoryDTO.setName(fields[1]);

                Category category = CategoryMapper.toEntity(categoryDTO);
                importedCategories.add(category);
            }
        } catch (IOException e) {
            logger.error("Ошибка при импорте категорий: {}", e.getMessage());
            throw e;
        }
        return importedCategories;
    }

    public byte[] categoriesToCsvBytes(List<CategoryDTO> categories) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            for (CategoryDTO category : categories) {
                writer.write(categoryToCsv(category));
                writer.newLine();
            }
        }
        return outputStream.toByteArray();
    }

    private String categoryToCsv(CategoryDTO categoryDTO) {
        return categoryDTO.getId() + "," + categoryDTO.getName();
    }
}