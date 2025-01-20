package com.example.mapper;

import com.example.dto.StoreDTO;
import com.example.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store toEntity(StoreDTO storeDTO);

    StoreDTO toDTO(Store store);

    @Mapping(target = "id", ignore = true) // Игнорируем ID, так как он не должен обновляться
    void updateEntity(StoreDTO storeDTO, @MappingTarget Store store);
}
