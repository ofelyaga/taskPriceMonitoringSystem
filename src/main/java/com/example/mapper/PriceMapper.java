package com.example.mapper;

import com.example.dto.PriceDTO;
import com.example.model.Price;
import com.example.model.Product;
import com.example.model.Store;

public class PriceMapper {

    public static Price toEntity(PriceDTO priceDTO) {
        Price price = new Price();
        price.setValue(priceDTO.getValue());
        price.setDate(priceDTO.getDate());
        Product product = new Product();
        product.setId(priceDTO.getProductId());
        price.setProduct(product);
        Store store = new Store();
        store.setId(priceDTO.getStoreId());
        price.setStore(store);

        return price;
    }

    public static PriceDTO toDTO(Price price) {
        return new PriceDTO(
                price.getId(),
                price.getValue(),
                price.getDate(),
                price.getProduct().getId(),
                price.getStore().getId()
        );
    }

}