package com.example.MySQL_Practicce.service;

import com.example.MySQL_Practicce.domain.Member;
import com.example.MySQL_Practicce.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3 {

    private final DataSource dataSource;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId,String toId,int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false); //start transaction

            logic(fromId, toId, money, con);
            con.commit(); //success
        }catch(Exception e){
            con.rollback(); //fail
            throw new IllegalStateException(e);
        }finally {
            release(con);
        }
    }

    private void logic(String fromId, String toId, int money, Connection con) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

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

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("get exception");
        }
    }
}
