package com.orm.hibernate.jta.env;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;

public class JPATest extends TransactionManagerTest {

    public String persistenceUnitName;
    public String[] hbmResources;
    public JPASetup JPA;

    @BeforeEach
    public void beforeClass() throws Exception {
        if (persistenceUnitName == null || persistenceUnitName.isEmpty()) {
            configurePersistenceUnit();
        }
    }

    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit(null);
    }

    public void configurePersistenceUnit(String persistenceUnitName,
                                         String... hbmResources) throws Exception {
        this.persistenceUnitName = persistenceUnitName;
        this.hbmResources = hbmResources;
    }

    @BeforeEach
    public void beforeMethod() throws Exception {
        JPA = new JPASetup(TM.databaseProduct, persistenceUnitName, hbmResources);
        // Always drop the schema, cleaning up at least some of the artifacts
        // that might be left over from the last run, if it didn't cleanup
        // properly
        JPA.dropSchema();

        JPA.createSchema();
        afterJPABootstrap();
    }

    public void afterJPABootstrap() throws Exception {
    }

    @AfterEach
    public void afterMethod() throws Exception {
        if (JPA != null) {
            beforeJPAClose();
            if (!"true".equals(System.getProperty("keepSchema"))) {
                JPA.dropSchema();
            }
            JPA.getEntityManagerFactory().close();
        }
    }

    public void beforeJPAClose() throws Exception {

    }

    protected long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    protected String getTextResourceAsString(String resource) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
        if (is == null) {
            throw new IllegalArgumentException("Resource not found: " + resource);
        }
        StringWriter sw = new StringWriter();
        copy(new InputStreamReader(is), sw);
        return sw.toString();
    }

    protected Throwable unwrapRootCause(Throwable throwable) {
        return unwrapCauseOfType(throwable, null);
    }

    protected Throwable unwrapCauseOfType(Throwable throwable, Class<? extends Throwable> type) {
        for (Throwable current = throwable; current != null; current = current.getCause()) {
            if (type != null && type.isAssignableFrom(current.getClass()))
                return current;
            throwable = current;
        }
        return throwable;
    }
}
