package com.example.MySQL_Practicce.repository;

import com.example.MySQL_Practicce.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;

@Slf4j
public class MemberRepositoryTestV2 {

    MemberRepositoryV2 repositoryV2;

    @BeforeEach
    void beforeEach(){
        //DriverManager: get new connection each time
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);

        //connection pooling: reuse connection
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repositoryV2=new MemberRepositoryV2(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("member2",30000);
        repositoryV2.save(member);

        //findById
        Member findMember = repositoryV2.findById("member2");
        log.info("findMember={}",findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        //update money: 10000->20000
        repositoryV2.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryV2.findById(member.getMemberId());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repositoryV2.delete(member.getMemberId());
        Assertions.assertThatThrownBy(()-> repositoryV2.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
