package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientCacheImpl;
import ru.otus.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        long t1 = System.currentTimeMillis();
        var client = dbServiceClient.saveClient(new Client("dbServiceClient"));

        for (int i = 0; i < 10; i++) {
            var clientSecondSelected = dbServiceClient
                    .getClient(client.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
            log.info("clientSecondSelected:{}", clientSecondSelected);
        }

        long t2 = System.currentTimeMillis();
        log.info("Total time: {}", t2 - t1);

        var dbServiceClientCache = new DbServiceClientCacheImpl(transactionManager, clientTemplate, new MyCache<>());
        t1 = System.currentTimeMillis();
        var clientCache = dbServiceClientCache.saveClient(new Client("dbServiceClientCache"));

        for (int i = 0; i < 10; i++) {
            var clientSecondSelected = dbServiceClientCache
                    .getClient(clientCache.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientCache.getId()));
            log.info("clientSecondSelected:{}", clientSecondSelected);
        }

        t2 = System.currentTimeMillis();
        log.info("Total time with cache: {}", t2 - t1);
    }
}
