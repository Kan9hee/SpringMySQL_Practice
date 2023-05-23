package com.example.MySQL_Practicce.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;

@Slf4j
public class ConnectionUtil {
    public static Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            log.info("get connection={}, class={}",connection,connection.getClass());
            return connection;
        }catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }

}
