package com.orm.hibernate.ex.model.entitymanager.transaction;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public enum DatabaseProduct {
    POSTGRESQL(
            (ds, connectionURL) -> {
                ds.setClassName("org.postgresql.xa.PGXADataSource");
                if (connectionURL != null) {
                    throw new IllegalArgumentException(
                            "PostgreSQL XADataSource doesn't support connection URLs"
                    );
                }
                ds.getDriverProperties().put("serverName", "localhost");
                ds.getDriverProperties().put("databaseName", "hibernatelearn");
                ds.getDriverProperties().put("user", "postgres");
                ds.getDriverProperties().put("password", "password");
            },
            org.hibernate.dialect.PostgreSQL82Dialect.class.getName()
    );

    public DataSourceConfiguration configuration;
    public String hibernateDialect;

    DatabaseProduct(DataSourceConfiguration configuration, String hibernateDialect) {
        this.configuration = configuration;
        this.hibernateDialect = hibernateDialect;
    }

    public interface DataSourceConfiguration {
        void configure(PoolingDataSource ds, String connectionURL);
    }
}
