package com.lwerl.executor.service;

import com.lwerl.executor.dao.GenericDAO;
import com.lwerl.executor.model.UserDataSet;

public class DBServiceImpl implements DBService {

    private GenericDAO dao;

    public DBServiceImpl(GenericDAO dao) {
        this.dao = dao;
    }

    @Override
    public void saveUser(UserDataSet user) {
        dao.save(user);
    }

    @Override
    public UserDataSet getUser(Long id) {
        return dao.load(id, UserDataSet.class);
    }
}
