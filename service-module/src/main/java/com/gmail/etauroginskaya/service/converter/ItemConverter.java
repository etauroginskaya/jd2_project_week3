package com.gmail.etauroginskaya.service.converter;

import com.gmail.etauroginskaya.data.model.Item;
import com.gmail.etauroginskaya.service.model.ItemDTO;

public interface ItemConverter {

    ItemDTO toDTO(Item item);

    Item fromDTO(ItemDTO itemDTO);
}
