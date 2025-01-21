package com.example.service;

import com.example.dto.PriceDTO;
import com.example.mapper.PriceMapper;
import com.example.model.Price;
import com.example.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PriceService {
    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    @Autowired
    public PriceService(PriceRepository priceRepository, PriceMapper priceMapper){
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    public PriceDTO addPrice(PriceDTO priceDTO) {
        Price price = priceMapper.toEntity(priceDTO);
        Price savedPrice = priceRepository.save(price);
        return priceMapper.toDTO(savedPrice);
    }

    public List<PriceDTO> getPriceHistory(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        return priceRepository.findByProductIdAndStoreIdAndDateBetween(productId, storeId, startDate, endDate).stream()
                .map(priceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PriceDTO getCurrentPrice(UUID productId, UUID storeId) {
        Price price = priceRepository.findFirstByProductIdAndStoreIdOrderByDateDesc(productId, storeId);
        return priceMapper.toDTO(price);
    }

    public List<PriceDTO> comparePrices(UUID productId, List<String> storeIds) {
        return priceRepository.findByProductIdAndStoreIdIn(productId, storeIds).stream()
                .map(priceMapper::toDTO)
                .collect(Collectors.toList());
    }
}