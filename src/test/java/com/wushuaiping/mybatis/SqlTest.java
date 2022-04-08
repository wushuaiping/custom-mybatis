package com.wushuaiping.mybatis;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @author wushuaiping
 * @date 2022/4/7 2:28 PM
 */
public class SqlTest {

    @Test
    public void testSelectOne(){
        try(Reader reader = Resources.getResourceAsReader("datasource.xml");
            SqlSession session = new SqlSessionFactoryBuilder().build(reader).openSession();
        ){
            User user = session.selectOne("com.wushuaiping.mybatis.queryUserInfoById", 1L);
            System.out.println(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectAll(){
        try(Reader reader = Resources.getResourceAsReader("datasource.xml");
            SqlSession session = new SqlSessionFactoryBuilder().build(reader).openSession();
        ){
           List<User> users = session.selectOne("com.wushuaiping.mybatis.queryUserInfoById");
            for (User user : users) {
                System.out.println(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
