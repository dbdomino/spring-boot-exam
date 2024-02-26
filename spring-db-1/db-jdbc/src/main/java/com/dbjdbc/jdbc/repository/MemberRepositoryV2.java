package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV2 {
    // jdbc  - DataSource 사용, JdbcUtils 사용
    private final DataSource dataSource; // 외부에서 DataSource 를 주입 받아서 사용한다. 이제 직접 만든 DBConnectionUtil 을 사용하지 않아도 된다.

    public MemberRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Member save(Member member) throws SQLException {
        String sql = "insert into member2(member_id, money) values(?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
//            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
            // 2개 안닫으면, 커넥션이 계속 유지되서 문제될 수 있음.
            // finally에서 exception이 터지면, throw되는데, pstmt.close()가 문제가 생기면, con.close()가 안될수 있다.
            // 해결방법 메소드만들어서 close 한번에 순차적으로 처리하자
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member2 where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public Member findById(Connection con, String memberId) throws SQLException {
        String sql = "select * from member2 where member_id = ?";
//        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
//            con = getConnection(); // 핵심, 새로운커넥션 요청하면 안되고 외부에서 준걸로 써야한다.
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
//            close(con, pstmt, rs); // 커넥션 닫으면 안된다. 매 쿼리 수행때마다 커넥션 닫게끔 하면 안된다. 필요한 때 닫도록 해줘야 한다.
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member2 set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }
    public void update(Connection con, String memberId, int money) throws SQLException {
        String sql = "update member2 set money=? where member_id=?";
//        Connection con = null;
        PreparedStatement pstmt = null;
        try {
//            con = getConnection();  //  핵심, 새로운커넥션 요청하면 안되고 외부에서 준걸로 써야한다.
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
//            close(con, pstmt, null);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member2 where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    //save()...
    //findById()...
    //update()....
    //delete()....

    // close 편리하게 JdbcUtils 의 메소드를 활용하기
    private void close(Connection con, Statement stmt, ResultSet rs) {
        // 핵심
        // JdbcUtils로 close메서드를 수행하면 connection 풀에 든 것 이었다면, connection을 닫는 게 아니라 connectionPool 로 반환한다.는데?
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get Connection={}, class={}", con, con.getClass());
        return con;
    }
}
