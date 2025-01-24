package com.example.controller;

import com.example.dto.*;
import com.example.mapper.CategoryMapper;
import com.example.mapper.PriceMapper;
import com.example.mapper.ProductMapper;
import com.example.mapper.StoreMapper;
import com.example.model.Category;
import com.example.model.Price;
import com.example.model.Product;
import com.example.model.Store;
import com.example.service.CategoryService;
import com.example.service.PriceService;
import com.example.service.ProductService;
import com.example.service.StoreService;
import com.example.utility.CsvUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/csv")
public class CsvExportImportController {

    private final CsvUtil csvUtil;
    private final ProductService productService;
    private final PriceService priceService;
    private final StoreService storeService;
    private final CategoryService categoryService;

    public CsvExportImportController(CsvUtil csvUtil, ProductService productService, PriceService priceService,
                                     StoreService storeService, CategoryService categoryService) {
        this.csvUtil = csvUtil;
        this.productService = productService;
        this.priceService = priceService;
        this.storeService = storeService;
        this.categoryService = categoryService;
    }

    @PostMapping("/import/products")
    public ResponseEntity<?> importProducts(@RequestParam("file") MultipartFile file) {
        try {
            List<Product> importedProducts = csvUtil.importProducts(file.getInputStream());
            for(Product product : importedProducts){
                productService.addProduct(ProductMapper.toDTO(product));
            }
            return ResponseEntity.ok(new SuccessResponse("Успешно импортировано " + importedProducts.size() + " продуктов из CSV"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при импорте продуктов: " + e.getMessage()));
        }
    }

    @GetMapping(value = "/export/products")
    public ResponseEntity<?> exportProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            byte[] content = csvUtil.productsToCsvBytes(products);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.csv")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(content.length)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при экспорте продуктов: " + e.getMessage()));
        }
    }

    @PostMapping("/import/prices")
    public ResponseEntity<?> importPrices(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("prices", ".csv");
            file.transferTo(tempFile);

            List<Price> importedPrices = csvUtil.importPrices(tempFile.getAbsolutePath());
            for(Price price : importedPrices){
                priceService.addPrice(PriceMapper.toDTO(price));
            }
            tempFile.delete();

            return ResponseEntity.ok(new SuccessResponse("Успешно импортировано " + importedPrices.size() + " цен из CSV"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при импорте цен: " + e.getMessage()));
        }
    }

    @GetMapping("/export/prices")
    public ResponseEntity<?> exportPrices() {
        try {
            List<PriceDTO> prices = priceService.getAllPrices();
            byte[] content = csvUtil.pricesToCsvBytes(prices);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prices.csv")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(content.length)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при экспорте цен: " + e.getMessage()));
        }
    }

    @PostMapping("/import/stores")
    public ResponseEntity<?> importStores(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("stores", ".csv");
            file.transferTo(tempFile);

            List<Store> importedStores = csvUtil.importStores(tempFile.getAbsolutePath());
            for(Store store : importedStores){
                storeService.addStore(StoreMapper.toDTO(store));
            }
            tempFile.delete();

            return ResponseEntity.ok(new SuccessResponse("Успешно импортировано " + importedStores.size() + " магазинов из CSV"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при импорте магазинов: " + e.getMessage()));
        }
    }

    @GetMapping("/export/stores")
    public ResponseEntity<?> exportStores() {
        try {
            List<StoreDTO> stores = storeService.getAllStores();
            byte[] content = csvUtil.storesToCsvBytes(stores);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stores.csv")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(content.length)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при экспорте магазинов: " + e.getMessage()));
        }
    }

    @PostMapping("/import/categories")
    public ResponseEntity<?> importCategories(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("categories", ".csv");
            file.transferTo(tempFile);

            List<Category> importedCategories = csvUtil.importCategories(tempFile.getAbsolutePath());
            for(Category category : importedCategories){
                categoryService.addCategory(CategoryMapper.toDTO(category));
            }
            tempFile.delete();

            return ResponseEntity.ok(new SuccessResponse("Успешно импортировано " + importedCategories.size() + " категорий из CSV"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при импорте категорий: " + e.getMessage()));
        }
    }

    @GetMapping("/export/categories")
    public ResponseEntity<?> exportCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            byte[] content = csvUtil.categoriesToCsvBytes(categories);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=categories.csv")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(content.length)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при экспорте категорий: " + e.getMessage()));
        }
    }
}