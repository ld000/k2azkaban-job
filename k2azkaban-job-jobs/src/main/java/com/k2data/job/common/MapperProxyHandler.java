package com.k2data.job.common;

import com.k2data.platform.exception.InternalException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lidong 12/1/16.
 */
public class MapperProxyHandler<T> implements InvocationHandler {

    private Class<T> proxyObj;

    public MapperProxyHandler(Class<T> proxyObj) {
        this.proxyObj = proxyObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSessionFactory sqlSessionFactory = PersistenceHelper.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            return method.invoke(sqlSession.getMapper(proxyObj), args);
        } catch (Exception e) {
            throw new InternalException(e);
        } finally {
            sqlSession.close();
        }
    }

}
