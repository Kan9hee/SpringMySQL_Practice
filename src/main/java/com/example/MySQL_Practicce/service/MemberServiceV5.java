package com.example.MySQL_Practicce.service;

import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class MemberServiceV5 {

    //private final PlatformTransactionManager transactionManager;
    //private final TransactionTemplate template;
    private final RepositoryException memberRepository;

    public MemberServiceV5(RepositoryException memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId,String toId,int money){
        logic(fromId, toId, money);
    }

    private void logic(String fromId, String toId, int money){
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("get exception");
        }
    }
}
