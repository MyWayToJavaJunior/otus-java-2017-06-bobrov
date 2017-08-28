package com.lwerl.orm.service;

import com.lwerl.cache.CacheEngine;
import com.lwerl.cache.MyElement;
import com.lwerl.cache.SoftCacheEngineImpl;
import com.lwerl.orm.dao.UserDAO;
import com.lwerl.orm.model.UserDataSet;


public class DBServiceImpl implements DBService {

    private static volatile DBService instance;

    private UserDAO userDAO;
    private final CacheEngine<Long, UserDataSet> cache;

    private DBServiceImpl() {
        userDAO = new UserDAO();
        cache = new SoftCacheEngineImpl<>(50, 500, 0);
    }

    public static DBService getInstance() {
        if (instance == null) {
            synchronized (DBServiceImpl.class) {
                if (instance == null) {
                    instance = new DBServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void saveUser(UserDataSet user) {
        if (user.getId() != null) {
            cache.evict(user.getId());
        }
        userDAO.saveUser(user);
    }

    @Override
    public UserDataSet getUser(Long id) {
        UserDataSet cached = cache.get(id);
        if (cached == null) {
            cached = userDAO.getUser(id);
            cache.put(new MyElement<>(id, cached));
        }
        return cached;
    }

    @Override
    public CacheEngine getCache() {
        return cache;
    }
}
