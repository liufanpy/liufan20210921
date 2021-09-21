package com.itheima.service.impl;

import com.itheima.dao.AccountDao;
import com.itheima.service.AccountService;
import com.itheima.utils.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AccountServiceImpl implements AccountService {

    //因为service要调用Dao，所以需要让spring注入进来dao
    @Autowired
    private AccountDao dao;

    //因为service要操作事务，所以需要让spring注入进来事务管理工具类
    /*@Autowired
    private TransactionManager tm;*/

    public void transfer(String from, String to, int money) throws SQLException {
         //2. 执行转账操作
         //扣钱
         dao.kouqian(from , money);
         int a = 1 / 0 ;
         //加钱
         dao.jiaqian(to , money);
    }






    /**
     * 转账方法
     * @param from
     * @param to
     * @param money
     */
   /* public void transfer(String from, String to, int money) {

        try {
            //1. 开启事务
            tm.startTransaction();

            //2. 执行转账操作
            //扣钱
            dao.kouqian(from , money);

            int a = 1 / 0 ;

            //加钱
            dao.jiaqian(to , money);

            //3. 提交事务
            tm.commit();
            tm.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
            try {
                //4. 回滚事务
                tm.rollback();
                tm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }*/
}
