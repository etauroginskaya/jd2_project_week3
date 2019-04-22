package com.gmail.etauroginskaya.service;

import com.gmail.etauroginskaya.service.model.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO add(ItemDTO itemDTO);

    List<ItemDTO> getItems();

    int update(Long id, String status);

    String getStatusById(Long id);
}
