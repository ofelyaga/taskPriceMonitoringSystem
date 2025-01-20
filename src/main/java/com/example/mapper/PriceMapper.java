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

    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
    @Mapping(target = "store", source = "storeId", qualifiedByName = "mapStoreIdToStore")
    Price toEntity(PriceDTO priceDTO);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "storeId", source = "store.id")
    PriceDTO toDTO(Price price);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
    @Mapping(target = "store", source = "storeId", qualifiedByName = "mapStoreIdToStore")
    void updateEntity(PriceDTO priceDTO, @MappingTarget Price price);

    default Product mapProductIdToProduct(UUID productId) {
        if (productId == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    default Store mapStoreIdToStore(UUID storeId) {
        if (storeId == null) {
            return null;
        }
        Store store = new Store();
        store.setId(storeId);
        return store;
    }
}