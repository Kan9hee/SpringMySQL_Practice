package com.example.MySQL_Practicce.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driveManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        Connection con2 = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        log.info("connection={}, class={}",con1,con1.getClass());
        log.info("connection={}, class={}",con2,con1.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        //DriverManagerDataSource - 항상 새로운 커넥션을 획득
        DataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException{
         Connection con1 = dataSource.getConnection();
         Connection con2 = dataSource.getConnection();
        log.info("connection={}, class={}",con1,con1.getClass());
        log.info("connection={}, class={}",con2,con1.getClass());
    }
}