package com.example.MySQL_Practicce.repository;

import com.example.MySQL_Practicce.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MemberRepositoryTest {

    MemberRepository repository = new MemberRepository();

    @Test
    void crud() throws SQLException {
        Member member = new Member("member1",10000);
        repository.save(member);
    }
}
