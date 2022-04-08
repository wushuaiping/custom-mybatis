package com.wushuaiping.mybatis;


/**
 * @author wushuaiping
 * @date 2022/3/1 10:37 AM
 */
public class DefaultSessionFactory implements SqlSessionFactory{

    private final Configuration configuration;

    public DefaultSessionFactory(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration.getConnection(), configuration.getMapperElement());
    }
}
