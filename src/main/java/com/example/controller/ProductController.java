package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.addProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String productId,
            @RequestBody ProductDTO updatedProductDTO
    ) {
        ProductDTO updatedProduct = productService.updateProduct(UUID.fromString(productId), updatedProductDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(UUID.fromString(productId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(UUID.fromString(categoryId));
        return ResponseEntity.ok(products);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(@RequestParam String name) {
        List<ProductDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDTO>> filterProductsByManufacturer(@RequestParam String manufacturer) {
        List<ProductDTO> products = productService.filterProductsByManufacturer(manufacturer);
        return ResponseEntity.ok(products);
    }
}
