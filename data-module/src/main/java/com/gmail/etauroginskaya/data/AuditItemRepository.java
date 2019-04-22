package com.gmail.etauroginskaya.data;

import com.gmail.etauroginskaya.data.model.AuditItem;

import java.sql.Connection;

public interface AuditItemRepository {
    AuditItem save(Connection connection, AuditItem item);
}
