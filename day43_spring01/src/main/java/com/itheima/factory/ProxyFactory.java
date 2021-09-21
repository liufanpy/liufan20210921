package com.itheima.factory;

/*
    1. 这是一个代理的工厂类，它里面有一个方法，这个方法专门创建出来AccountServiceImpl的代理对象，然后让spring管理起来

    2. 具体的步骤：
        2.1  代理对象要和真实对象对接的，所以需要让spring把真实对象注入到这个类里面来。

        2.2  创建代理对象的方法，它的返回值需要被spring管理起来，所以这个方法需要打上注解@Bean

        2.3 要想让spring感知到这个带有@Bean注解的方法，并且会调用这个方法，执行代理的创建工作，
            那么这个ProxyFactory这个类也需要交给spring管理。

        2.4 由于代理对象调用真实对象的转账方法的时候，需要用到事务来包裹这个转账的方法，所以
            还需要让spring把TransactionManager给注入进来。
 */

import com.itheima.service.AccountService;
import com.itheima.utils.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

@Component
public class ProxyFactory {


    //需要注入进来真实对象
    @Autowired
    private AccountService accountServiceImpl;

    //转入事务管理员
    @Autowired
    private TransactionManager tm;


    //创建一个方法，用于生成代理对象，并且把这个代理对象交给spring管理
    @Bean
    public AccountService proxyService(){
        return (AccountService) Proxy.newProxyInstance(
                accountServiceImpl.getClass().getClassLoader(),
                accountServiceImpl.getClass().getInterfaces(),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        System.out.println("来执行invoke方法了~~需要给转账的方法加上事务" + method.getName());

                        Object result = null;

                        if(method.getName().equals("transfer")){
                            try {
                                //在执行真实对象方法之前，先开启事务
                                tm.startTransaction();

                                //调用真实对象的转账方法
                                result = method.invoke(accountServiceImpl , args);

                                //提交事务
                                tm.commit();
                                tm.close();
                            } catch (Exception e) {
                                e.printStackTrace();

                                //回滚事务
                                tm.rollback();
                                tm.close();
                            }

                        }else{
                            result = method.invoke(accountServiceImpl , args);
                        }
                        return result;
                    }
                }
        );
    }
}
