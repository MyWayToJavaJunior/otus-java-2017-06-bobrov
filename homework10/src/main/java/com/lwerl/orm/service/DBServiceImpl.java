package com.lwerl.orm.service;

import com.lwerl.orm.dao.UserDAO;
import com.lwerl.orm.model.UserDataSet;


public class DBServiceImpl implements DBService {

    private static volatile DBService instance;

    private UserDAO userDAO;

    private DBServiceImpl() {
        userDAO = new UserDAO();
    }

    public static DBService getInstance() {
        DBService localInstance = instance;
        if (localInstance == null) {
            synchronized (DBServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DBServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void saveUser(UserDataSet user) {
        userDAO.saveUser(user);
    }

    @Override
    public UserDataSet getUser(Long id) {
        return userDAO.getUser(id);
    }
}
