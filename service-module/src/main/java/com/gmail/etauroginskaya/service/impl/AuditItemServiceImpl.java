package com.gmail.etauroginskaya.service.impl;

import com.gmail.etauroginskaya.data.AuditItemRepository;
import com.gmail.etauroginskaya.service.AuditItemService;
import com.gmail.etauroginskaya.service.connection.ConnectionHandler;
import com.gmail.etauroginskaya.service.exception.DatabaseException;
import com.gmail.etauroginskaya.data.model.AuditItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class AuditItemServiceImpl implements AuditItemService {

    private static final Logger logger = LoggerFactory.getLogger(AuditItemServiceImpl.class);
    private static final String CONNECTION_ERROR_MESSAGE = "Connection Failed! Check output console.";
    private final ConnectionHandler connectionHandler;
    private final AuditItemRepository auditItemRepository;

    @Autowired
    public AuditItemServiceImpl(ConnectionHandler connectionHandler, AuditItemRepository auditItemRepository) {
        this.connectionHandler = connectionHandler;
        this.auditItemRepository = auditItemRepository;
    }

    @Override
    public AuditItem save(AuditItem item) {
        try (Connection connection = connectionHandler.getConnection()) {
            connection.setAutoCommit(false);
            try {
                AuditItem saveAuditItem = auditItemRepository.save(connection, item);
                connection.commit();
                return saveAuditItem;
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
