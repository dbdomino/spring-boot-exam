package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import com.dbjdbc.exception.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 인터페이스 활용 V4_1 - 예외 누수(예외에 대한 의존) 해결하기
 * Repository 인터페이스로 실행하기 위해, 인터페이스 기반으로 JDBC용 리포지터리 새로 만듬.
 * 큰그림으로 보면 Repository는 JDBC에 의존하지 않게 하려고, JPA로 바꾸더라도 service로직 또는 빈에 구현체만 바꾸면 되도록 하려고
 * 핵심. implements MemberRepository 인터페이스 기반 구현체로 만듬, throws SQLException 제거
 */
@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository{
    // jdbc  - DataSource 사용, JdbcUtils 사용
    private final DataSource dataSource; // 외부에서 DataSource 를 주입 받아서 사용한다. 이제 직접 만든 DBConnectionUtil 을 사용하지 않아도 된다.

    public MemberRepositoryV4_1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member)  {
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
            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
//            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }

    public Member findById(String memberId) {
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
            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
//            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money)  {
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
            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
//            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) {
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
            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
//            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    // close 편리하게 JdbcUtils 의 메소드를 활용하기
    private void close(Connection con, Statement stmt, ResultSet rs) {
        // JdbcUtils로 close메서드를 수행하면 connection 풀에 든 것 이었다면, connection을 닫는 게 아니라 connectionPool 로 반환한다.는데?
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);// 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
//        JdbcUtils.closeConnection(con); // .
    }

    private Connection getConnection() throws SQLException {
        // 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다. 직접 꺼내지 않는다. 핵심.
        Connection con = DataSourceUtils.getConnection(dataSource);   // TransactionSynchronizationManager.getResource(dataSource); 해준다.
        log.info("get connection={} class={}", con, con.getClass());
        return con;
    }
}
