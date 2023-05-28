package com.example.MySQL_Practicce.service;

import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.MemberRepositoryV4;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class MemberServiceTestV4_1 {

    public static final String member1="member1";
    public static final String member2="member2";
    public static final String exception="ex";

    @Autowired
    private MemberRepositoryV4 memberRepository;
    @Autowired
    private MemberServiceV4 memberService;

    @TestConfiguration
    static class TestConfig{
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource){
            this.dataSource=dataSource;
        }

        @Bean
        MemberRepositoryV4 memberRepositoryV4(){
            return new MemberRepositoryV4(dataSource);
        }

        @Bean
        MemberServiceV4 memberServiceV4(){
            return new MemberServiceV4(memberRepositoryV4());
        }
    }

    /*
    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME,PASSWORD);
        memberRepository=new MemberRepositoryV4(dataSource);
        memberService = new MemberServiceV4(memberRepository);
    }
    */

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        memberRepository.delete(exception);
    }

    @Test
    void AopCheck(){
        log.info("memberService class={}",memberService.getClass());
        log.info("memberRepository class={}",memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    } //proxy

    @Test
    @DisplayName("normal operation")
    void accountTransfer() throws SQLException {
        Member member_1 = new Member(member1,10000);
        Member member_2 = new Member(member2,10000);

        memberRepository.save(member_1);
        memberRepository.save(member_2);


        assertThatThrownBy(()->memberService.accountTransfer(member_1.getMemberId(), member_2.getMemberId(),2000))
                .isInstanceOf(IllegalStateException.class);

        Member resultMember_1=memberRepository.findById(member_1.getMemberId());
        Member resultMember_2=memberRepository.findById(member_2.getMemberId());

        assertThat(resultMember_1.getMoney()).isEqualTo(8000);
        assertThat(resultMember_2.getMoney()).isEqualTo(12000);
    }
}