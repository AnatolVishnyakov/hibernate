package com.orm.hibernate.jta.env;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class TransactionManagerTest {
    // Static single database connection manager per test suite
    static public TransactionManagerSetup TM;

    @BeforeAll
    public static void beforeSuite() throws Exception {
        TM = new TransactionManagerSetup(DatabaseProduct.POSTGRESQL);
    }

    @AfterAll
    public static void afterSuite() throws Exception {
        if (TM != null)
            TM.stop();
    }
}
