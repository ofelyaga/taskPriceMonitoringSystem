package com.example.service;

import com.example.dto.PriceChartDTO;
import com.example.dto.PriceComparisonDTO;
import com.example.dto.PriceDTO;
import com.example.exception.AppException;
import com.example.mapper.PriceMapper;
import com.example.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final PriceRepository priceRepository;

    @Autowired
    public AnalyticsService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<PriceDTO> getPriceTrends(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        try {
            logger.info("Запрос на получение трендов цен для продукта с ID {}, магазина с ID {} с {} по {}",
                    productId, storeId, startDate, endDate);
            List<PriceDTO> priceTrends = priceRepository.findByProductIdAndStoreIdAndDateBetween(productId, storeId, startDate, endDate)
                    .stream()
                    .map(PriceMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно получены тренды цен: количество = {}", priceTrends.size());
            return priceTrends;
        } catch (Exception e) {
            logger.error("Ошибка при получении трендов цен для продукта с ID {}, магазина с ID {}: {}",
                    productId, storeId, e.getMessage(), e);
            throw new AppException("Ошибка при получении трендов цен", e);
        }
    }

    public List<PriceComparisonDTO> comparePrices(UUID productId, List<String> storeIds) {
        try {
            logger.info("Запрос на сравнение цен для продукта с ID {} в магазинах: {}", productId, storeIds);
            List<PriceComparisonDTO> priceComparisons = priceRepository.findByProductIdAndStoreIdIn(productId, storeIds)
                    .stream()
                    .map(price -> new PriceComparisonDTO(price.getDate(), price.getValue(), price.getStore().getName()))
                    .collect(Collectors.toList());
            logger.info("Успешно выполнено сравнение цен: количество = {}", priceComparisons.size());
            return priceComparisons;
        } catch (Exception e) {
            logger.error("Ошибка при сравнении цен для продукта с ID {} в магазинах {}: {}",
                    productId, storeIds, e.getMessage(), e);
            throw new AppException("Ошибка при сравнении цен", e);
        }
    }

    public PriceChartDTO getPriceChartData(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        try {
            logger.info("Запрос на получение данных для графика цен для продукта с ID {}, магазина с ID {} с {} по {}",
                    productId, storeId, startDate, endDate);
            List<PriceDTO> priceTrends = getPriceTrends(productId, storeId, startDate, endDate);

            List<String> dates = priceTrends.stream()
                    .map(PriceDTO::getDate)
                    .map(LocalDate::toString)
                    .collect(Collectors.toList());

            List<Double> prices = priceTrends.stream()
                    .map(PriceDTO::getValue)
                    .collect(Collectors.toList());

            PriceChartDTO priceChartDTO = new PriceChartDTO(dates, prices);
            logger.info("Успешно получены данные для графика цен: количество точек = {}", prices.size());
            return priceChartDTO;
        } catch (Exception e) {
            logger.error("Ошибка при получении данных для графика цен для продукта с ID {}, магазина с ID {}: {}",
                    productId, storeId, e.getMessage(), e);
            throw new AppException("Ошибка при получении данных для графика цен", e);
        }
    }
}