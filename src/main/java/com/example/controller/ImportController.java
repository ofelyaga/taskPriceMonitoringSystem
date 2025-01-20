package com.example.controller;

import com.example.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/import")
public class ImportController {

    @Autowired
    private ImportService importService;

    @PostMapping("/products/csv")
    @ResponseBody
    public ResponseEntity<Void> importProductsFromCsv(@RequestParam("file") MultipartFile file) {
        importService.importProductsFromCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/prices/json")
    @ResponseBody
    public ResponseEntity<Void> importPricesFromJson(@RequestParam("file") MultipartFile file) {
        importService.importPricesFromJson(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}