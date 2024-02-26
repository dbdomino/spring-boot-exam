package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

/**
 * 인터페이스 활용 V5 - 스프링 예외 변환기(예외 추상화) + JDBC templete
 * 템플릿 콜백 패턴을 사용한 JDBC 탬플릿으로 con, preparedStatement 사용 반복을 없애보자.
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {
    // jdbc  - DataSource 사용, JdbcUtils 사용
//    private final DataSource dataSource; // 외부에서 DataSource 를 주입 받아서 사용한다. 이제 직접 만든 DBConnectionUtil 을 사용하지 않아도 된다.
//    private final SQLExceptionTranslator exceptionTranslator; // 길면 exTranslator 로 변수명해도됨.
    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Member save(Member member)  {
        String sql = "insert into member2(member_id, money) values(?, ?)";
        template.update(sql, member.getMemberId(), member.getMoney()); // 아래소스 다 이거하나로 대체가능함.
        return member;
        // con연결 preparedStatement , resultSet, close문, trycatch 까지 template 하나로 대체가능
    }

    public Member findById(String memberId) {
        String sql = "select * from member2 where member_id = ?";
        Member member = template.queryForObject(sql, memberRowMapper(), memberId);
        return member;
    }
    // RowMapper 는 JdbcTemplate에서 사용하는 인터페이스
    private RowMapper<Member> memberRowMapper() { // queryForObject 라는 JDBC 인터페이스 메소드 사용위해 선언함. RowMapper<T>가 필요함.
        return (rs, rowNum) -> { // 결과가 resultSet이 나오면 memberRowMapper()로 주입시켜 결과를 만들어 준다.
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }

    public void update(String memberId, int money)  {
        String sql = "update member2 set money=? where member_id=?";
        template.update(sql, money, memberId);
    }

    public void delete(String memberId) {
        String sql = "delete from member2 where member_id=?";
        template.update(sql, memberId);
    }
}
