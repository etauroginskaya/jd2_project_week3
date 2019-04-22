package com.gmail.etauroginskaya.service.impl;

import com.gmail.etauroginskaya.data.ItemRepository;
import com.gmail.etauroginskaya.data.model.Item;
import com.gmail.etauroginskaya.service.ItemService;
import com.gmail.etauroginskaya.service.connection.ConnectionHandler;
import com.gmail.etauroginskaya.service.converter.ItemConverter;
import com.gmail.etauroginskaya.service.exception.DatabaseException;
import com.gmail.etauroginskaya.service.model.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private static final String CONNECTION_ERROR_MESSAGE = "Connection Failed! Check output console.";
    private final ConnectionHandler connectionHandler;
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;

    @Autowired
    public ItemServiceImpl(ConnectionHandler connectionHandler, ItemRepository itemRepository, ItemConverter itemConverter) {
        this.connectionHandler = connectionHandler;
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionHandler.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = itemConverter.fromDTO(itemDTO);
                Item saveItem = itemRepository.add(connection, item);
                ItemDTO saveItemDTO = itemConverter.toDTO(saveItem);
                connection.commit();
                return saveItemDTO;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
        }
    }

    @Override
    public List<ItemDTO> getItems() {
        try (Connection connection = connectionHandler.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Item> items = itemRepository.getItems(connection);
                List<ItemDTO> itemsDTO = items.stream()
                        .map(item -> itemConverter.toDTO(item))
                        .collect(Collectors.toList());
                connection.commit();
                return itemsDTO;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
        }
    }

    @Override
    public int update(Long id, String status) {
        try (Connection connection = connectionHandler.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int countUpdateItem = itemRepository.update(connection, id, status);
                connection.commit();
                return countUpdateItem;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
        }
    }

    @Override
    public String getStatusById(Long id) {
        try (Connection connection = connectionHandler.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String statusItem = itemRepository.getItemStatusById(connection, id);
                connection.commit();
                return statusItem;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
        }
    }
}
