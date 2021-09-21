package com.itheima.service;

import java.sql.SQLException;

public interface AccountService {

    void transfer(String from , String to , int money) throws SQLException;
}
