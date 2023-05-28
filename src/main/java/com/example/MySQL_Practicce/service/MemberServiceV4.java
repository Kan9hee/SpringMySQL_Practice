package com.example.MySQL_Practicce.service;

import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.MemberRepositoryV4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
public class MemberServiceV4 {

    //private final PlatformTransactionManager transactionManager;
    //private final TransactionTemplate template;
    private final MemberRepositoryV4 memberRepository;


    public MemberServiceV4(MemberRepositoryV4 memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId,String toId,int money) throws SQLException {
        logic(fromId, toId, money);
    }

    private void logic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    /*
    private static void release(Connection con) {
        if(con !=null){
            try {
                con.setAutoCommit(true);
                con.close();
            }catch (Exception e){
                log.info("error",e);
            }
        }
    }
    */

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("get exception");
        }
    }
}
