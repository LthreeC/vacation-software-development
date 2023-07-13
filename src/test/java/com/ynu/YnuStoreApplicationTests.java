package com.ynu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class YnuStoreApplicationTests {
    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
    }

    /**
     * 数据库连接池 DBCP C3P0 Hikari据说世界最快
     * HikariProxyConnection@668707379 wrapping com.mysql.cj.jdbc.ConnectionImpl@65d73bd
     * @throws SQLException
     */
    @Test
    void getConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }
}
