package com.gmail.etauroginskaya.data.impl;

import com.gmail.etauroginskaya.data.ItemRepository;
import com.gmail.etauroginskaya.data.exception.DatabaseQueryException;
import com.gmail.etauroginskaya.data.model.Item;
import com.gmail.etauroginskaya.data.model.ItemStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);
    private static final String QUERY_ERROR_MESSAGE = "SQL query Failed! Check output console.";

    @Override
    public Item add(Connection connection, Item item) {
        String sql = "INSERT INTO item(name, status) VALUES(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getStatusEnum().name());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Creating item failed (query " + sql + "), no row affected.");
                throw new DatabaseQueryException("Creating item failed (query " + sql + "), no row affected.");
            }
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return getAddedItem(rs, item);
                } else {
                    logger.error("Creating item failed (query " + sql + "), no ID obtained.");
                    throw new DatabaseQueryException("Creating item failed (query " + sql + "), no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseQueryException(QUERY_ERROR_MESSAGE);
        }
    }

    @Override
    public List<Item> getItems(Connection connection) {
        String sql = "SELECT * FROM item";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Item> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(getItem(rs));
                }
                return items;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseQueryException(QUERY_ERROR_MESSAGE);
        }
    }

    @Override
    public int update(Connection connection, Long id, String newStatus) {
        String sql = "UPDATE item SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setLong(2, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseQueryException(QUERY_ERROR_MESSAGE);
        }
    }

    @Override
    public String getItemStatusById(Connection connection, Long id) {
        String sql = "SELECT status FROM item WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString(1);
                    return status;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseQueryException(QUERY_ERROR_MESSAGE);
        }
        return null;
    }

    Item getAddedItem(ResultSet resultSet, Item item) throws SQLException {
        Item saveItem = new Item();
        saveItem.setId(resultSet.getLong(1));
        saveItem.setName(item.getName());
        saveItem.setStatusEnum(item.getStatusEnum());
        return saveItem;
    }

    Item getItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(resultSet.getString("name"));
        item.setStatusEnum(ItemStatusEnum.valueOf(resultSet.getString("status")));
        return item;
    }
}
