package com.example.controller;

import com.example.dto.PriceChartDTO;
import com.example.dto.PriceComparisonDTO;
import com.example.dto.PriceTrendDTO;
import com.example.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/trends")
    @ResponseBody
    public ResponseEntity<List<PriceTrendDTO>> getPriceTrends(
            @RequestParam UUID productId,
            @RequestParam String storeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<PriceTrendDTO> priceTrends = analyticsService.getPriceTrends(productId, UUID.fromString(storeId), startDate, endDate);
        return ResponseEntity.ok(priceTrends);
    }

    @GetMapping("/compare")
    @ResponseBody
    public ResponseEntity<List<PriceComparisonDTO>> comparePrices(
            @RequestParam UUID productId,
            @RequestParam List<String> storeIds
    ) {
        List<PriceComparisonDTO> priceComparisons = analyticsService.comparePrices(productId, storeIds);
        return ResponseEntity.ok(priceComparisons);
    }

    @GetMapping("/chart")
    @ResponseBody
    public ResponseEntity<PriceChartDTO> getPriceChartData(
            @RequestParam UUID productId,
            @RequestParam String storeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        PriceChartDTO priceChartData = analyticsService.getPriceChartData(productId, UUID.fromString(storeId), startDate, endDate);
        return ResponseEntity.ok(priceChartData);
    }
}