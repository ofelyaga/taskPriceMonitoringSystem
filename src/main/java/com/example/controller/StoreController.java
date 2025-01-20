package com.example.controller;

import com.example.dto.StoreDTO;
import com.example.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<StoreDTO> addStore(@RequestBody StoreDTO storeDTO) {
        StoreDTO savedStore = storeService.addStore(storeDTO);
        return new ResponseEntity<>(savedStore, HttpStatus.CREATED);
    }

    @PutMapping("/{storeId}")
    @ResponseBody
    public ResponseEntity<StoreDTO> updateStore(
            @PathVariable String storeId,
            @RequestBody StoreDTO updatedStoreDTO
    ) {
        StoreDTO updatedStore = storeService.updateStore(UUID.fromString(storeId), updatedStoreDTO);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/{storeId}")
    @ResponseBody
    public ResponseEntity<Void> deleteStore(@PathVariable String storeId) {
        storeService.deleteStore(UUID.fromString(storeId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        List<StoreDTO> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }
}