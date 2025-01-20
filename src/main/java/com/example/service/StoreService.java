package com.example.service;

import com.example.dto.StoreDTO;
import com.example.mapper.StoreMapper;
import com.example.model.Store;
import com.example.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public StoreDTO addStore(StoreDTO storeDTO) {
        Store store = storeMapper.toEntity(storeDTO);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toDTO(savedStore);
    }

    public StoreDTO updateStore(UUID storeId, StoreDTO updatedStoreDTO) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        storeMapper.updateEntity(updatedStoreDTO, store);
        Store updatedStore = storeRepository.save(store);
        return storeMapper.toDTO(updatedStore);
    }

    public void deleteStore(UUID storeId) {
        storeRepository.deleteById(storeId);
    }

    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(storeMapper::toDTO)
                .collect(Collectors.toList());
    }
}