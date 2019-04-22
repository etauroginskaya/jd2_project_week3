package com.gmail.etauroginskaya.data.impl;

import com.gmail.etauroginskaya.data.AuditItemRepository;
import com.gmail.etauroginskaya.data.exception.DatabaseQueryException;
import com.gmail.etauroginskaya.data.model.AuditItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class AuditItemRepositoryImpl implements AuditItemRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);
    private static final String QUERY_ERROR_MESSAGE = "SQL query Failed! Check output console.";

    @Override
    public AuditItem save(Connection connection, AuditItem item) {
        String sql = "INSERT INTO audit(item_id, `action`, `date`) VALUES(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getItem_id());
            statement.setString(2, item.getAction());
            statement.setString(3, item.getDate());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Creating audit item failed, no row affected.");
                throw new DatabaseQueryException("Creating audit item failed, no row affected.");
            }
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    AuditItem saveAuditItem = new AuditItem();
                    saveAuditItem.setId(rs.getString(1));
                    saveAuditItem.setAction(item.getAction());
                    saveAuditItem.setDate(item.getDate());
                    saveAuditItem.setItem_id(item.getItem_id());
                    return saveAuditItem;
                } else {
                    logger.error("Creating audit item: " + item + " failed, no ID obtained.");
                    throw new DatabaseQueryException("Creating audit item: " + item + " failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseQueryException(QUERY_ERROR_MESSAGE);
        }
    }
}
