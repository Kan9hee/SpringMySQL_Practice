package com.example.MySQL_Practicce.repository;

import com.example.MySQL_Practicce.connection.ConnectionUtil;
import com.example.MySQL_Practicce.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MemberRepository {

    public Member save(Member member) throws SQLException {
        String sql="insert into member(member_id, money) values (?, ?)";

        Connection con=null;
        PreparedStatement preparedStatement=null;

        try{
            con=getConnection();
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,member.getMemberId());
            preparedStatement.setInt(2,member.getMoney());
            int count = preparedStatement.executeUpdate();
            return member;
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,preparedStatement,null);
        }
    }

    private void close(Connection con, Statement state, ResultSet res){

        if(res!=null){
            try {
                res.close();
            }catch (SQLException e){
                log.info("error",e);
            }
        }

        if(state!=null){
            try {
                state.close();
            }catch (SQLException e){
                log.info("error",e);
            }
        }

        if(con!=null){
            try {
                con.close();
            }catch (SQLException e){
                log.info("error",e);
            }
        }
    }
    private Connection getConnection(){
        return ConnectionUtil.getConnection();
    }
}
