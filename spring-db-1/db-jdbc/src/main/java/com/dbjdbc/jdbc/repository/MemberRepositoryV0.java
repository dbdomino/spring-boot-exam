package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.dbjdbc.jdbc.connection.DBConnectionUtil.getConnection;

@Slf4j
public class MemberRepositoryV0 {
    // jdbc driverManager 로 커넥션 직접 생성
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
            close(pstmt, con, null);
            // 2개 안닫으면, 커넥션이 계속 유지되서 문제될 수 있음.
            // finally에서 exception이 터지면, throw되는데, pstmt.close()가 문제가 생기면, con.close()가 안될수 있다.
            // 해결방법 메소드만들어서 close 순차적으로 처리하자.
        }

    }

    private void close(PreparedStatement pstmt, Connection con, ResultSet rs) throws SQLException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (pstmt != null) { // 이거 굳이 쓰는이유, null로도 받을 수 있도록 만들기 위해서.
            try {
                pstmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
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
                throw new NoSuchElementException("member not found memberId=" +
                        memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(pstmt,con, rs);
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
            close(pstmt, con, null);
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
            close(pstmt, con, null);
        }
    }
}
