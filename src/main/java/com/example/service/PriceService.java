package com.example.service;

import com.example.dto.PriceDTO;
import com.example.exception.AppException;
import com.example.mapper.PriceMapper;
import com.example.model.Price;
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
public class PriceService {

    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public PriceDTO addPrice(PriceDTO priceDTO) {
        try {
            logger.info("Запрос на добавление цены: {}", priceDTO);
            Price price = PriceMapper.toEntity(priceDTO);
            Price savedPrice = priceRepository.save(price);
            logger.info("Цена успешно добавлена: {}", savedPrice);
            return PriceMapper.toDTO(savedPrice);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении цены: {}", priceDTO, e);
            throw new AppException("Ошибка при добавлении цены", e);
        }
    }

    public List<PriceDTO> getPriceHistory(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        try {
            logger.info("Запрос на получение истории цен для продукта с ID {} и магазина с ID {} с {} по {}",
                    productId, storeId, startDate, endDate);
            List<PriceDTO> prices = priceRepository.findByProductIdAndStoreIdAndDateBetween(productId, storeId, startDate, endDate)
                    .stream()
                    .map(PriceMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно получена история цен: количество = {}", prices.size());
            return prices;
        } catch (Exception e) {
            logger.error("Ошибка при получении истории цен для продукта с ID {} и магазина с ID {}: {}",
                    productId, storeId, e.getMessage(), e);
            throw new AppException("Ошибка при добавлении цены", e);
        }
    }

    public PriceDTO getCurrentPrice(UUID productId, UUID storeId) {
        try {
            logger.info("Запрос на получение текущей цены для продукта с ID {} и магазина с ID {}", productId, storeId);
            Price price = priceRepository.findFirstByProductIdAndStoreIdOrderByDateDesc(productId, storeId);
            if (price == null) {
                logger.warn("Текущая цена для продукта с ID {} и магазина с ID {} не найдена", productId, storeId);
                throw new RuntimeException("Текущая цена не найдена");
            }
            logger.info("Текущая цена успешно получена: {}", price);
            return PriceMapper.toDTO(price);
        } catch (Exception e) {
            logger.error("Ошибка при получении текущей цены для продукта с ID {} и магазина с ID {}: {}",
                    productId, storeId, e.getMessage(), e);
            throw new AppException("Ошибка при добавлении цены", e);
        }
    }

    public List<PriceDTO> comparePrices(UUID productId, List<String> storeIds) {
        try {
            logger.info("Запрос на сравнение цен для продукта с ID {} в магазинах: {}", productId, storeIds);
            List<PriceDTO> prices = priceRepository.findByProductIdAndStoreIdIn(productId, storeIds)
                    .stream()
                    .map(PriceMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Успешно выполнено сравнение цен: количество = {}", prices.size());
            return prices;
        } catch (Exception e) {
            logger.error("Ошибка при сравнении цен для продукта с ID {} в магазинах {}: {}",
                    productId, storeIds, e.getMessage(), e);
            throw new AppException("Ошибка при добавлении цены", e);
        }
    }
}