package com.example.MySQL_Practicce.repository;

import com.example.MySQL_Practicce.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class MemberRepositoryTest {

    MemberRepository repository = new MemberRepository();

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("member1",10000);
        //epository.save(member);

        //findById
        Member findMember = repository.findById("member1");
        log.info("findMember={}",findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        //update money: 10000->20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);
    }
}
