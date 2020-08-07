package io.quarkus.ext.querydsl.runtime;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * Get database connection
 * 
 * @author Leo Tu
 */
public class ConnectionProvider implements Provider<Connection> {

    final private DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection get() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
