package com.gmail.etauroginskaya.data;

import com.gmail.etauroginskaya.data.model.Item;

import java.sql.Connection;
import java.util.List;

public interface ItemRepository {
    Item add(Connection connection, Item item);

    List<Item> getItems(Connection connection);

    int update(Connection connection, Long id, String newStatus);

    String getItemStatusById(Connection connection, Long id);
}
