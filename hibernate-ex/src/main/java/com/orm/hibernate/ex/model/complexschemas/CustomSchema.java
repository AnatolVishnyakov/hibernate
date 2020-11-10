package com.orm.hibernate.ex.model.complexschemas;

import org.hibernate.boot.model.relational.AbstractAuxiliaryDatabaseObject;
import org.hibernate.dialect.Dialect;

public class CustomSchema extends AbstractAuxiliaryDatabaseObject {
    public CustomSchema() {
        addDialectScope("org.hibernate.dialect.PostgreSQLDialect");
    }

    @Override
    public String[] sqlCreateStrings(Dialect dialect) {
        return new String[]{"[CREATE statement]"};
    }

    @Override
    public String[] sqlDropStrings(Dialect dialect) {
        return new String[]{"[DROP statement]"};
    }
}