package com.lwerl.executor.service;

import com.lwerl.executor.model.UserDataSet;

public interface DBService {

    void saveUser(UserDataSet user);

    UserDataSet getUser(Long id);
}
