package service;

import model.UserDataSet;

public interface DBService {

    void saveUser(UserDataSet user);

    UserDataSet getUser(Long id);
}
