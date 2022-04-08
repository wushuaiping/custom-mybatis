package com.wushuaiping.mybatis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wushuaiping
 * @date 2022/3/1 3:49 PM
 */
public class SqlSessionFactoryBuilder {

    public DefaultSessionFactory build(Reader reader) {
        SAXReader saxReader = new SAXReader();
        // 设置不联网时的XML解析方式
        saxReader.setEntityResolver(new XMLMapperEntityResolver());
        try {
            Document document = saxReader.read(new InputSource(reader));
            // 解析xml中的配置信息
            Configuration configuration = parseConfiguration(document.getRootElement());
            return new DefaultSessionFactory(configuration);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析配置
     *
     * @param root 根节点
     * @return 配置信息
     */
    private Configuration parseConfiguration(Element root) {
        Configuration configuration = new Configuration();
        // 解析并设置数据源信息
        configuration.setDataSource(dataSource(root.selectNodes("//dataSource")));
        // 解析并设置连接信息
        configuration.setConnection(connection(configuration.getDataSource()));
        // 解析并设值mapper元素信息
        configuration.setMapperElement(mapperElement(root.selectNodes("mappers")));
        return configuration;
    }

    /**
     * 获取数据源信息
     *
     * @param nodes 元素
     * @return 数据源信息
     */
    private Map<String, String> dataSource(List<Node> nodes) {
        Map<String, String> dataSource = new HashMap<>(4);
        Element element = (Element) nodes.get(0);
        for (Element e : element.elements()) {
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            dataSource.put(name, value);
        }
        return dataSource;
    }

    /**
     * 解析xml
     *
     * @param elements xml元素
     * @return 解析后的元素信息
     */
    private Map<String, XNode> mapperElement(List<Node> nodes) {
        Map<String, XNode> map = new HashMap<>();
        Element element = (Element) nodes.get(0);
        for (Element e : element.elements()) {
            String resource = e.attributeValue("resource");
            try {
                Reader reader = Resources.getResourceAsReader(resource);
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();
                //命名空间
                String namespace = root.attributeValue("namespace");
                List<Element> select = root.elements("select");
                for (Element node : select) {
                    String id = node.attributeValue("id");
                    String parameterType = node.attributeValue("parameterType");
                    String resultType = node.attributeValue("resultType");
                    String sql = node.getText();
                    // ? 匹配
                    Map<Integer, String> parameter = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sql = sql.replace(g1, "?");
                    }

                    XNode xNode = new XNode();
                    xNode.setNamespace(namespace);
                    xNode.setId(id);
                    xNode.setParameterType(parameterType);
                    xNode.setResultType(resultType);
                    xNode.setSql(sql);
                    xNode.setParameter(parameter);
                    map.put(namespace + "." + id, xNode);
                }
            } catch (IOException | DocumentException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取连接信息
     *
     * @param dataSource 数据源配置
     * @return 连接信息
     */
    private Connection connection(Map<String, String> dataSource) {
        try {
            Class.forName(dataSource.get("driver"));
            return DriverManager.getConnection(dataSource.get("url"), dataSource.get("username"), dataSource.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
