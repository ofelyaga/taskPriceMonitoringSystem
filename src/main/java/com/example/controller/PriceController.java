package com.example.controller;

import com.example.dto.PriceDTO;
import com.example.service.PriceService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<PriceDTO> addPrice(@RequestBody PriceDTO priceDTO) {
        PriceDTO savedPrice = priceService.addPrice(priceDTO);
        return new ResponseEntity<>(savedPrice, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @ResponseBody
    public ResponseEntity<List<PriceDTO>> getPriceHistory(
            @RequestParam UUID productId,
            @RequestParam String storeId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate date1 = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate date2 = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<PriceDTO> priceHistory = priceService.getPriceHistory(productId, UUID.fromString(storeId), date1, date2);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/current")
    @ResponseBody
    public ResponseEntity<PriceDTO> getCurrentPrice(
            @RequestParam UUID productId,
            @RequestParam UUID storeId
    ) {
        PriceDTO currentPrice = priceService.getCurrentPrice(productId, storeId);
        return ResponseEntity.ok(currentPrice);
    }

    @GetMapping("/compare")
    @ResponseBody
    public ResponseEntity<List<PriceDTO>> comparePrices(
            @RequestParam UUID productId,
            @RequestParam List<String> storeIds
    ) {
        List<PriceDTO> priceComparisons = priceService.comparePrices(productId, storeIds);
        return ResponseEntity.ok(priceComparisons);
    }
}