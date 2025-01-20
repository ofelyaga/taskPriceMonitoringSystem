package com.example.service;

import com.example.dto.PriceChartDTO;
import com.example.dto.PriceComparisonDTO;
import com.example.dto.PriceTrendDTO;
import com.example.mapper.PriceTrendMapper;
import com.example.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final PriceRepository priceRepository;
    private final PriceTrendMapper priceTrendMapper;

    public List<PriceTrendDTO> getPriceTrends(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        return priceRepository.findByProductIdAndStoreIdAndDateBetween(productId, storeId, startDate, endDate).stream()
                .map(priceTrendMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<PriceComparisonDTO> comparePrices(UUID productId, List<String> storeIds) {
        return priceRepository.findByProductIdAndStoreIdIn(productId, storeIds).stream()
                .map(price -> new PriceComparisonDTO(
                        price.getDate(),
                        price.getValue(),
                        price.getStore().getName()
                ))
                .collect(Collectors.toList());
    }

    public PriceChartDTO getPriceChartData(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        List<PriceTrendDTO> priceTrends = getPriceTrends(productId, storeId, startDate, endDate);

        List<String> dates = priceTrends.stream()
                .map(PriceTrendDTO::getDate)
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        List<Double> prices = priceTrends.stream()
                .map(PriceTrendDTO::getValue)
                .collect(Collectors.toList());

        return new PriceChartDTO(dates, prices);
    }
}