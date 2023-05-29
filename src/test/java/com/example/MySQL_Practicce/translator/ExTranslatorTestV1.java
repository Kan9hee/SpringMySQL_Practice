package com.example.MySQL_Practicce.translator;

import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.ex.DuplicateKeyException;
import com.example.MySQL_Practicce.repository.ex.MyDbException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;

@Slf4j
public class ExTranslatorTestV1 {

    Repository repository;
    Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave(){
        service.create("testId");
        service.create("testId");
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service{
        private final Repository repository;

        public void create(String memberId){
            try {
                repository.save(new Member(memberId, 0));
                log.info("saveId={}", memberId);
            }catch (DuplicateKeyException e){
                log.info("key duplicated");
                String retryId = generateNewId(memberId);
                log.info("retryId={}",retryId);
                repository.save(new Member(retryId,0));
            }catch (MyDbException e){
                log.info("data access layer exception",e);
                throw e;
            }
        }

        private String generateNewId(String memberId){
            return memberId+new Random().nextInt(10000);
        }
    }

    @RequiredArgsConstructor
    static class Repository{
        private final DataSource dataSource;

        public Member save(Member member){
            String message = "insert into member(member_id, money) values(?,?)";
            Connection con = null;
            PreparedStatement statement = null;
            try{
                con = dataSource.getConnection();
                statement = con.prepareStatement(message);
                statement.setString(1,member.getMemberId());
                statement.setInt(2,member.getMoney());
                statement.executeUpdate();
                return member;
            }catch (SQLException e){
                if(e.getErrorCode() == 1062){
                    throw new DuplicateKeyException(e);
                }
                throw new MyDbException(e);
            }finally {
                JdbcUtils.closeStatement(statement);
                JdbcUtils.closeConnection(con);
            }
        }
    }
}
