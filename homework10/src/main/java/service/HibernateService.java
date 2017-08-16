package service;

import helper.HibernateHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateService {

    private static volatile HibernateService instance;
    private SessionFactory sessionFactory;

    private HibernateService() {
        sessionFactory = HibernateHelper.createSessionFactory();
    }

    public static HibernateService getInstance() {
        HibernateService localInstance = instance;
        if (localInstance == null) {
            synchronized (HibernateService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new HibernateService();
                }
            }
        }
        return localInstance;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
