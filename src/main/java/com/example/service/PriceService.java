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

    @Autowired
    public PriceService(PriceRepository priceRepository){
        this.priceRepository = priceRepository;
    }

    public PriceDTO addPrice(PriceDTO priceDTO) {
        Price price = PriceMapper.toEntity(priceDTO);
        Price savedPrice = priceRepository.save(price);
        return PriceMapper.toDTO(savedPrice);
    }

    public List<PriceDTO> getPriceHistory(UUID productId, UUID storeId, LocalDate startDate, LocalDate endDate) {
        return priceRepository.findByProductIdAndStoreIdAndDateBetween(productId, storeId, startDate, endDate).stream()
                .map(PriceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PriceDTO getCurrentPrice(UUID productId, UUID storeId) {
        Price price = priceRepository.findFirstByProductIdAndStoreIdOrderByDateDesc(productId, storeId);
        return PriceMapper.toDTO(price);
    }

    public List<PriceDTO> comparePrices(UUID productId, List<String> storeIds) {
        return priceRepository.findByProductIdAndStoreIdIn(productId, storeIds).stream()
                .map(PriceMapper::toDTO)
                .collect(Collectors.toList());
    }
}