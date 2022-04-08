package com.wushuaiping.mybatis;

/**
 * @author wushuaiping
 * @date 2022/3/1 10:37 AM
 */
public interface SqlSessionFactory {

    SqlSession openSession();

}