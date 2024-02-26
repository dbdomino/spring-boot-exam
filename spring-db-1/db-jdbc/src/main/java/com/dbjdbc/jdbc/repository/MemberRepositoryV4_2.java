package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 인터페이스 활용 V4_2 - 스프링 예외 변환기(예외 추상화)
 * 스프링 예외 변환기(예외 추상화) 기능으로 DB마다 제공하는 에러코드에 맞는 에러를 찾아 에러이름반환하는 기능을 이용하려 한다.
 * 핵심. SQLException에서 뱉는 에러코드를 기준으로, 에러코드와 DB별 기준 변환된 에러이름으로 던지는게 핵심.
 * //org.springframework.jdbc.support.sql-error-codes.xml 여기표기준같은거 참고
 *
 * 지금 V4_2는 JDBC에 의존되며 만들어지고 있지만, 어짜피 실행은 MemberRepository 인터페이스로 실행한다. 예외 누수는 아니다.
 * catch 에서 (BadSqlGrammarException e) 를 쓰든 (DuplicateKeyException e)를  쓰든 스프링이 제공하는 에러코드로 throw시키고, 서비스측에서
 * 해당 에러로 받게 해서 예외별로 처리되도록 하는 것 도 가능하다.(스프링에 의존하는 것이지만, JDBC든 Mybitis든 JPA든 다른 기술의 예외변환기를 제공해주고, 거기에 대한 적용이 편리해진다.)
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{
    // jdbc  - DataSource 사용, JdbcUtils 사용
    private final DataSource dataSource; // 외부에서 DataSource 를 주입 받아서 사용한다. 이제 직접 만든 DBConnectionUtil 을 사용하지 않아도 된다.
    private final SQLExceptionTranslator exceptionTranslator; // 길면 exTranslator 로 변수명해도됨.

    public MemberRepositoryV4_2(DataSource dataSource) {

        this.dataSource = dataSource;
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
        // 구현체는 다른것도 있으나, 굳이 SQLErrorCodeSQLExceptionTranslator 씀
        // SQLStateSQLExceptionTranslator 에러상태로 찾는걸로 쓸려면 이걸로 해야됨.
        // H2는 e.getErrorCode() 로 찾아졌지만, postgre는 e.getErrorCode() 는 0만 나오고, e.getSQLState() 로 찾아야 했다. 그땐 SQLStateSQLExceptionTranslator 구현체 써야함.
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
//            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
            throw exceptionTranslator.translate("save", sql, e);  // 알맞은 스프링 에러클래스로 던지도록 해줌.
            // argument 는 메소드어떤거 썻나, 쿼리는 뭔가, 에러는 뭔가 넣어주면 알아서 찾아줌.
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
//            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
            throw exceptionTranslator.translate("save", sql, e);  // 알맞은 스프링 에러클래스로 던지도록 해줌.
            // argument 는 메소드어떤거 썻나, 쿼리는 뭔가, 에러는 뭔가 넣어주면 알아서 찾아줌.
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
//            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
            throw exceptionTranslator.translate("save", sql, e);  // 알맞은 스프링 에러클래스로 던지도록 해줌.
            // argument 는 메소드어떤거 썻나, 쿼리는 뭔가, 에러는 뭔가 넣어주면 알아서 찾아줌.
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
//            throw new MyDbException(e); // 익셉션 전환, 익셉션 포함.
            throw exceptionTranslator.translate("save", sql, e);  // 알맞은 스프링 에러클래스로 던지도록 해줌.
            // argument 는 메소드어떤거 썻나, 쿼리는 뭔가, 에러는 뭔가 넣어주면 알아서 찾아줌.
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
