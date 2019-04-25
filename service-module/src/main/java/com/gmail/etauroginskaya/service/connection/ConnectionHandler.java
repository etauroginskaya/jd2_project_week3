package com.gmail.etauroginskaya.service.connection;

import com.gmail.etauroginskaya.service.exception.DatabaseDriverException;
import com.gmail.etauroginskaya.service.exception.DatabaseException;
import com.gmail.etauroginskaya.service.exception.DocumentReadException;
import com.gmail.etauroginskaya.service.exception.FileNotFoundException;
import com.gmail.etauroginskaya.data.properties.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ConnectionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);
    private static final String CONNECTION_ERROR_MESSAGE = "Connection Failed! Check output console.";
    private static final String IO_ERROR_MESSAGE = "Reading SQL-file Failed! Check output console.";
    private static final String FILE_ERROR_MESSAGE = "Script to create database tables not found!";
    private final DatabaseProperties databaseProperties;

    @Autowired
    public ConnectionHandler(DatabaseProperties databaseProperties) {
        try {
            Class.forName(databaseProperties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error("Driver not found", e);
            throw new DatabaseDriverException("Driver not found");
        }
        this.databaseProperties = databaseProperties;
    }

    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", databaseProperties.getUsername());
            properties.setProperty("password", databaseProperties.getPassword());
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "cp1251");
            return DriverManager.getConnection(databaseProperties.getUrl(), properties);
        } catch (SQLException e) {
            logger.error(CONNECTION_ERROR_MESSAGE, e);
            throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
        }
    }

    @PostConstruct
    private void createDatabaseTables() {
        String scriptName = getClass().getResource("/" + databaseProperties.getScript()).getPath();
        List<String> listQueries = getListQueries(scriptName);
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                for (String query : listQueries) {
                    statement.addBatch(query);
                }
                statement.executeBatch();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(CONNECTION_ERROR_MESSAGE, e);
                throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(CONNECTION_ERROR_MESSAGE, e);
            throw new DatabaseException(CONNECTION_ERROR_MESSAGE);
        }
    }

    List<String> getListQueries(String fileName) {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
                String line;
                List<String> listQueries = new ArrayList<>();
                while ((line = in.readLine()) != null) {
                    listQueries.add(line);
                }
                return listQueries;
            } catch (java.io.FileNotFoundException e) {
                logger.error(FILE_ERROR_MESSAGE, e);
                throw new FileNotFoundException(FILE_ERROR_MESSAGE);
            }
        } catch (IOException e) {
            logger.error(IO_ERROR_MESSAGE, e);
            throw new DocumentReadException(IO_ERROR_MESSAGE);
        }
    }
}

