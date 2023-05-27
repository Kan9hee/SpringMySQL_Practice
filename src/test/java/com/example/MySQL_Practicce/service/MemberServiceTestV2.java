package com.example.MySQL_Practicce.service;

import com.example.MySQL_Practicce.connection.ConnectionSet;
import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.MemberRepositoryV2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.example.MySQL_Practicce.connection.ConnectionSet.*;
import static org.assertj.core.api.Assertions.*;

class MemberServiceTestV2 {

    public static final String member1="member1";
    public static final String member2="member2";
    public static final String exception="ex";

    private MemberRepositoryV2 memberRepository;
    private MemberService memberService;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME,PASSWORD);
        memberRepository=new MemberRepositoryV2(dataSource);
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        memberRepository.delete(exception);
    }

    @Test
    @DisplayName("normal operation")
    void accountTransfer() throws SQLException {
        Member member_1 = new Member(member1,10000);
        Member member_2 = new Member(member2,10000);
        //Member member_ex = new Member(exception,10000);
        memberRepository.save(member_1);
        memberRepository.save(member_2);
        //memberRepository.save(member_ex);

        assertThatThrownBy(()->memberService.accountTransfer(member_1.getMemberId(), member_2.getMemberId(),2000))
                .isInstanceOf(IllegalStateException.class);

        Member resultMember_1=memberRepository.findById(member_1.getMemberId());
        Member resultMember_2=memberRepository.findById(member_2.getMemberId());
        //Member resultException=memberRepository.findById(member_ex.getMemberId());
        assertThat(resultMember_1.getMoney()).isEqualTo(8000);
        assertThat(resultMember_2.getMoney()).isEqualTo(12000);
    }
}