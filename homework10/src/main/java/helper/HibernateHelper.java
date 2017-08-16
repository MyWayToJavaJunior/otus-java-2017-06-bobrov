package helper;

import model.AddressDataSet;
import model.DataSet;
import model.PhoneDataSet;
import model.UserDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class HibernateHelper {

    private final static String HIBERNATE_CONFIG_FILE = "hibernate.properties";

    private static Configuration makeHibernateConfiguration() {
        Configuration result = new Configuration();
        ClassLoader classLoader = HibernateHelper.class.getClassLoader();
        URL file = classLoader.getResource(HIBERNATE_CONFIG_FILE);
        if (file != null) {
            try (InputStream input = file.openStream()) {
                Properties properties = new Properties();
                properties.load(input);
                result.setProperties(properties);
                result.addAnnotatedClass(DataSet.class);
                result.addAnnotatedClass(UserDataSet.class);
                result.addAnnotatedClass(AddressDataSet.class);
//                result.addAnnotatedClass(PhoneDataSet.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static SessionFactory createSessionFactory() {
        Configuration configuration = makeHibernateConfiguration();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
