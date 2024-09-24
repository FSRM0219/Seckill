package org.seckill.util;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * JDBC数据库连接工具类
 * */
@Slf4j
public class DBUtil {

    private static Properties props;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(in);
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static Connection getConn() throws Exception {
        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");
        String driver = props.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}
