package com.itheima.dao.impl;

import com.itheima.dao.AccountDao;
import com.itheima.utils.TransactionManager;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/*
    1. Dao需要操作数据库，所以需要用到QueryRunner ， 那么要让spring注入进来QueryRunner
    2. 操作数据库需要用到连接对象，为了确保service和dao使用的是同一个连接，
        所以也需要让spring把TransactionManager给注入进来
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private QueryRunner runner;

    @Autowired
    private TransactionManager tm;

    //以前的QueryRunner : QueryRunner runner = new QueryRunner(datasource);
    //今天的QueryRunner：  QueryRunner runner = new QueryRunner();

    /**
     * 扣钱
     * @param from
     * @param money
     */
    public void kouqian(String from, int money) throws SQLException {

        String sql = "update t_account set money = money - ? where name = ?";

        //这行代码是有错的，因为这个更新的操作并没有传递进去连接对象，
        //runner.update(sql , money , from);
        runner.update(tm.getConn() , sql , money , from);
    }

    public void jiaqian(String to, int money) throws SQLException {
        String sql = "update t_account set money = money + ? where name = ?";
        runner.update(tm.getConn() , sql , money , to);
    }
}
