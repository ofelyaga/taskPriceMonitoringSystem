package com.example.mapper;

import com.example.dto.PriceTrendDTO;
import com.example.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PriceTrendMapper {

    PriceTrendMapper INSTANCE = Mappers.getMapper(PriceTrendMapper.class);

    PriceTrendDTO toDTO(Price price);
}