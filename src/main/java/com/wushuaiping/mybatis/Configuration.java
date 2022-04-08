package com.wushuaiping.mybatis;

import java.sql.Connection;
import java.util.Map;

/**
 * @author wushuaiping
 * @date 2022/3/1 11:22 AM
 */
public class Configuration {

    private Connection connection;

    private Map<String, XNode> mapperElement;

    private Map<String, String> dataSource;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, XNode> getMapperElement() {
        return mapperElement;
    }

    public void setMapperElement(Map<String, XNode> mapperElement) {
        this.mapperElement = mapperElement;
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, String> getDataSource() {
        return dataSource;
    }
}
