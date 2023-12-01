package server;

import models.entities.crontask.NotificadorCron;
import org.quartz.SchedulerException;
import server.init.PersistidorGeoref;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import server.init.PersistidorRoles;

public class App {
    private static EntityManager entityManager = null;

    public static void main(String[] args) throws IOException, SchedulerException {
        new PersistidorGeoref().start(entityManager());
        new PersistidorRoles().start(entityManager());

        // Instanciamos todos los Cron Task
        new NotificadorCron().start();
        //new RankingsCron().start();

        Server.init(entityManager());
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        // https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<String, Object>();

        String[] keys = new String[] { "javax.persistence.jdbc.url", "javax.persistence.jdbc.user",
                "javax.persistence.jdbc.password", "javax.persistence.jdbc.driver", "hibernate.hbm2ddl.auto",
                "hibernate.connection.pool_size", "hibernate.show_sql", "hibernate.connection.autosave" };

        for (String key : keys) {
            if (env.containsKey(key)) {

                String value = env.get(key);
                configOverrides.put(key, value);

            }
        }

        // Agrego esto de conservative porque varias veces me salio el problema  cached plan must not change result type en servicio y prestacion de servicio
        configOverrides.put("hibernate.connection.autosave", "conservative");

        return Persistence.createEntityManagerFactory("db", configOverrides);
    }

    // Entity manager as Singleton
    public static EntityManager entityManager() {
        if(entityManager == null) {
            EntityManagerFactory entityMan = createEntityManagerFactory();
            entityManager = entityMan.createEntityManager();
            return entityManager;
        }
        return entityManager;
    }
}

