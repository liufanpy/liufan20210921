package com.itheima.test;

import com.itheima.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestAccountServiceImpl {


    //注入真实对象
    //@Autowired
    //private AccountService as;

    //注入代理对象
    @Autowired
    private AccountService  proxyService;


    @Test
    public void testTransfer() throws SQLException {

        //真实对象的转账
       // as.transfer("zs","ls",100);
        proxyService.transfer("zs","ls",100);
    }
}
