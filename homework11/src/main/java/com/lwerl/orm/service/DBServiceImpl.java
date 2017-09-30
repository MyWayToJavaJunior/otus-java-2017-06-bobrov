package com.lwerl.orm.service;

import com.lwerl.cache.CacheEngine;
import com.lwerl.cache.MyElement;
import com.lwerl.cache.SoftCacheEngineImpl;
import com.lwerl.messagesystem.Address;
import com.lwerl.messagesystem.Addressee;
import com.lwerl.messagesystem.MessageSystem;
import com.lwerl.orm.dao.UserDAO;
import com.lwerl.orm.model.UserDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBServiceImpl implements DBService {

    private UserDAO userDAO;
    private final CacheEngine<Long, UserDataSet> cache;
    private Address address;

    @Autowired
    public DBServiceImpl(MessageSystem system) {
        this.userDAO = new UserDAO();
        this.cache = new SoftCacheEngineImpl<>(100, 1000, 0);
        this.address = new Address();
        system.addAddressee(this);
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

    @Override
    public Address getAddress() {
        return address;
    }
}
