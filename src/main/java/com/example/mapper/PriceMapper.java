package com.example.mapper;

import com.example.dto.PriceDTO;
import com.example.model.Price;
import com.example.model.Product;
import com.example.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, StoreMapper.class})
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    Price toEntity(PriceDTO priceDTO);
    PriceDTO toDTO(Price price);

    void updateEntity(PriceDTO priceDTO, @MappingTarget Price price);
}