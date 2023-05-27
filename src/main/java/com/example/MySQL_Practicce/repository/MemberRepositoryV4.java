package com.example.MySQL_Practicce.repository;

import com.example.MySQL_Practicce.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV4 {

    private final DataSource dataSource;

    public MemberRepositoryV4(DataSource dataSource) {
        this.dataSource = dataSource;
    }


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

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con=null;
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;

        try{
            con=getConnection();
            preparedStatement=con.prepareStatement(sql);
            preparedStatement.setString(1,memberId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId="+memberId);
            }
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,preparedStatement,rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try{
            con=getConnection();
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,money);
            preparedStatement.setString(2,memberId);
            int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize={}",resultSize);
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,preparedStatement,null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try{
            con=getConnection();
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,memberId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,preparedStatement,null);
        }
    }

    private void close(Connection con, Statement state, ResultSet res){

        JdbcUtils.closeResultSet(res);
        JdbcUtils.closeStatement(state);
        DataSourceUtils.releaseConnection(con,dataSource); //keep connect,
    }

    private Connection getConnection() throws SQLException {

        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}",con,con.getClass());
        return con;
    }
}
