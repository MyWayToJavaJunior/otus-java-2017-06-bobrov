package com.lwerl.executor.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultMapper<T> {

    T map(ResultSet rowSet) throws SQLException, IllegalAccessException, InstantiationException;
}
