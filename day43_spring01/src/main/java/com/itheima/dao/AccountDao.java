package com.itheima.dao;

import java.sql.SQLException;

public interface AccountDao {

    void kouqian(String from , int money) throws SQLException;

    void jiaqian(String to , int money) throws SQLException;
}
