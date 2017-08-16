package com.lwerl.orm.service;

import com.lwerl.orm.model.UserDataSet;

public interface DBService {

    void saveUser(UserDataSet user);

    UserDataSet getUser(Long id);
}
