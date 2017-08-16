package com.lwerl.orm.helper;

import com.lwerl.orm.model.AddressDataSet;
import com.lwerl.orm.model.DataSet;
import com.lwerl.orm.model.PhoneDataSet;
import com.lwerl.orm.model.UserDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateHelper {

    private HibernateHelper() {
    }

    private static Configuration makeHibernateConfiguration() {
        Configuration result = new Configuration();
        result.addAnnotatedClass(DataSet.class);
        result.addAnnotatedClass(UserDataSet.class);
        result.addAnnotatedClass(AddressDataSet.class);
        result.addAnnotatedClass(PhoneDataSet.class);
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
