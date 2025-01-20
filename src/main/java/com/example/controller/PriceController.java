package com.example.controller;

import com.example.dto.PriceDTO;
import com.example.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<PriceDTO> priceHistory = priceService.getPriceHistory(productId, UUID.fromString(storeId), startDate, endDate);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/current")
    @ResponseBody
    public ResponseEntity<PriceDTO> getCurrentPrice(
            @RequestParam UUID productId,
            @RequestParam String storeId
    ) {
        PriceDTO currentPrice = priceService.getCurrentPrice(productId, UUID.fromString(storeId));
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