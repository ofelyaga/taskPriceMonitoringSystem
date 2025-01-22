package com.example.mapper;

import com.example.dto.StoreDTO;
import com.example.model.Store;

public class StoreMapper {

    public static Store toEntity(StoreDTO storeDTO) {
        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setCity(storeDTO.getCity());
        return store;
    }

    public static StoreDTO toDTO(Store store) {
        return new StoreDTO(
                store.getId(),
                store.getName(),
                store.getCity()
        );
    }

    public static void updateEntity(Store store, StoreDTO storeDTO) {
        if (storeDTO.getName() != null) {
            store.setName(storeDTO.getName());
        }
        if (storeDTO.getCity() != null) {
            store.setCity(storeDTO.getCity());
        }
    }
}