package dao;

import model.UserDataSet;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.HibernateService;

public class UserDAO {

    private HibernateService hibernateService = HibernateService.getInstance();

    public UserDataSet getUser(Long id) {
        Session session = hibernateService.getSession();
        UserDataSet result = session.get(UserDataSet.class, id);
        session.close();
        return result;
    }

    public void saveUser(UserDataSet user) {
        Session session = hibernateService.getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }
}
