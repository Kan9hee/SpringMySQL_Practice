package com.example.MySQL_Practicce.service;

import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId,String toId,int money) throws SQLException {
        Member fromMembber = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMembber.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("get exception");
        }
    }
}
