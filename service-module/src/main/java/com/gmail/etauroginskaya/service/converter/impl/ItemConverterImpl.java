package com.gmail.etauroginskaya.service.converter.impl;

import com.gmail.etauroginskaya.data.model.Item;
import com.gmail.etauroginskaya.data.model.ItemStatusEnum;
import com.gmail.etauroginskaya.service.converter.ItemConverter;
import com.gmail.etauroginskaya.service.model.ItemDTO;
import org.springframework.stereotype.Service;

@Service
public class ItemConverterImpl implements ItemConverter {
    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setStatus(item.getStatusEnum().name());
        return itemDTO;
    }

    @Override
    public Item fromDTO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setStatusEnum(ItemStatusEnum.valueOf(itemDTO.getStatus()));
        return item;
    }
}
