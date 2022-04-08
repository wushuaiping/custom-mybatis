package com.wushuaiping.mybatis;

import java.util.List;
import java.util.Objects;

/**
 * @author wushuaiping
 * @date 2022/2/28 3:29 PM
 */
public interface SqlSession extends AutoCloseable{

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T>List<T> selectList(String statement);

    <T>List<T> selectList(String statement, Object parameter);

    void close();
}
