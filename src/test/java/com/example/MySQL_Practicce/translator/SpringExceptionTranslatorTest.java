package com.example.MySQL_Practicce.translator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodes;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;

    @BeforeEach
    void init(){
        dataSource=new DriverManagerDataSource(URL,USERNAME,PASSWORD);
    }

    void sqlExceptionErrorCode(){
        String message = "select bad grammar";

        try{
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(message);
            preparedStatement.executeQuery();
        }catch(SQLException e){
            assertThat(e.getErrorCode()).isEqualTo(1062);
            int errorCode = e.getErrorCode();
            log.info("errorCode={}",errorCode);
            log.info("error",e);
        }
    }

    @Test
    void exceptionTranslator(){
        String message = "select bad grammar";
        try{
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(message);
            preparedStatement.executeQuery();
        }catch(SQLException e){
            assertThat(e.getErrorCode()).isEqualTo(1062);
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException resultEx = translator.translate("select",message,e);
            log.info("resultEx",resultEx);
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }
}
