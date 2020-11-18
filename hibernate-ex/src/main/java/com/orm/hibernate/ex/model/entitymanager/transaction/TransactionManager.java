package com.orm.hibernate.ex.model.entitymanager.transaction;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.orm.hibernate.ex.model.entitymanager.model.Item;
import org.slf4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class TransactionManager {
    private static final Logger logger = getLogger(TransactionManager.class);
    private static final String DATASOURCE_NAME = "BitronixTransactionEx";
    private final PoolingDataSource datasource;
    private final Context context;

    {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
        context = new InitialContext(props);
    }

    public TransactionManager() throws NamingException {
        this(DatabaseProduct.POSTGRESQL, null);
    }

    public TransactionManager(DatabaseProduct databaseProduct, String connectionURL) throws NamingException {
        logger.info("Starting database connection pool");

        logger.info("Setting stable unique identifier for transaction recovery");
        TransactionManagerServices.getConfiguration().setServerId("localhost");

        logger.info("Disabling JMX binding of manager in unit tests");
        TransactionManagerServices.getConfiguration().setDisableJmx(true);

        logger.info("Disabling transaction logging for unit tests");
        TransactionManagerServices.getConfiguration().setJournal("null");

        logger.info("Disabling warnings when the database isn't accessed in a transaction");
        TransactionManagerServices.getConfiguration().setWarnAboutZeroResourceTransaction(false);

        logger.info("Creating connection pool");
        datasource = new PoolingDataSource();
        datasource.setUniqueName(DATASOURCE_NAME);
        datasource.setMinPoolSize(1);
        datasource.setMaxPoolSize(5);
        datasource.setPreparedStatementCacheSize(10);

        // Our locking/versioning tests assume READ COMMITTED transaction
        // isolation. This is not the default on MySQL InnoDB, so we set
        // it here explicitly.
        datasource.setIsolationLevel("READ_COMMITTED");

        // Hibernate's SQL schema generator calls connection.setAutoCommit(true)
        // and we use auto-commit mode when the EntityManager is in suspended
        // mode and not joined with a transaction.
        datasource.setAllowLocalTransactions(true);

        logger.info("Setting up database connection: " + databaseProduct);
        databaseProduct.configuration.configure(datasource, connectionURL);

        logger.info("Initializing transaction and resource management");
        datasource.init();
    }

    public Context getNamingContext() {
        return context;
    }

    public UserTransaction getUserTransaction() {
        try {
            return (UserTransaction) getNamingContext()
                    .lookup("java:comp/UserTransaction");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public DataSource getDataSource() {
        try {
            return (DataSource) getNamingContext().lookup(DATASOURCE_NAME);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void rollback() {
        UserTransaction tx = getUserTransaction();
        try {
            if (tx.getStatus() == Status.STATUS_ACTIVE ||
                    tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                tx.rollback();
        } catch (Exception ex) {
            System.err.println("Rollback of transaction failed, trace follows!");
            ex.printStackTrace(System.err);
        }
    }

    public void stop() throws Exception {
        logger.info("Stopping database connection pool");
        datasource.close();
        TransactionManagerServices.getTransactionManager().shutdown();
    }

    public static void main(String[] args) throws NamingException {
        final TransactionManager tm = new TransactionManager();

        EntityManager em = null;
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("BitronixTransactionEx");
            em = emf.createEntityManager();

            // ...
            final Item item = new Item();
            em.persist(item);

            tx.commit();
        } catch (Exception exc) {
            try {
                if (tx.getStatus() == Status.STATUS_ACTIVE
                        || tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                    tx.rollback();
                }
            } catch (Exception rbExc) {
                logger.error("Rollback of transaction failed, trace follows!");
                rbExc.printStackTrace();
            }
            throw new RuntimeException(exc);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
