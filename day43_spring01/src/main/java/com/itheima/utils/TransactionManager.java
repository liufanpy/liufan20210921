package com.itheima.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
    这是一个事务管理的工具类，包含了事务的操作以及连接的提供

    1. 事务操作：
        1.1 开启事务
        1.2 提交事务
        1.3 回滚事务
    2. 连接操作：
        2.1 对外提供连接
        2.2 这个连接需要确保service和dao使用的是同一个。所以需要用上ThreadLocal

    3. 考虑IOC和DI的问题
        IOC的问题：
            3.1 这个类是一个事务管理的工具类，它能提供事务的操作，并且也能提供连接
                service和dao要想得到连接，必须要问这个类拿。禁止主动去问DataSource(连接池要)

            3.2 service和dao一定要持有TransactionManager这个类的对象，所以我们的办法就是
                把这个类交给spring来管理（托管），这样子就直接在service和dao里面注入即可。
         DI的问题：
            一定要让spring把连接池注入到TransactionManager这个类里面来，这样子，我们就可以在
               这里得到连接，然后去保存到ThreadLocal里面以及去操作事务了。
 */
@Component
public class TransactionManager {

    //定义一个ThreadLocal，确保连接是从这里面拿的
    ThreadLocal<Connection> threadLocal   = new ThreadLocal<Connection>();

    @Autowired
    private DataSource dataSource;  //让spring注入连接池


    //1. 提供一个方法，对外提供连接
    public Connection  getConn() throws SQLException {

        //1. 从ThreadLocal里面获取连接
        Connection conn = threadLocal.get();

        //2. 对连接对象进行判空操作，因为第一次来，ThreadLocal里面是没有东西的。
        if(conn == null){
            //3. 就问连接池要连接
            conn = dataSource.getConnection();

            //4. 把这个连接对象保存到ThreadLocal里面
            threadLocal.set(conn);
        }
        //返回连接对象
        return conn;
    }


    //2. 提供一个方法，用于开始事务
    public void startTransaction() throws SQLException {
        getConn().setAutoCommit(false);
    }

    //3. 提交事务
    public void commit() throws SQLException {
        getConn().commit();
    }

    //4. 回滚事务
    public void rollback() throws SQLException {
        getConn().rollback();
    }

    //5. 关闭连接
    public void close() throws SQLException {
        getConn().close();
    }
}
