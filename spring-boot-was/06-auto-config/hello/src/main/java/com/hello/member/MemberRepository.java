package com.hello.member;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
// jdbcTemplate로 crud 만들기
@Repository
public class MemberRepository {
    private final JdbcTemplate template;

    // DbConfig` 에서 `JdbcTemplate` 을 빈으로 등록했기 때문에 바로 주입받아서 사용할 수 있다.
    public MemberRepository(JdbcTemplate template) {
        this.template = template;
    }

    // 메모리 테이블 생성 (보통 리포지토리에 테이블을 생성하는 스크립트를 두지는 않는다.)
    public void initTable(){
        template.execute("create table member(member_id varchar primary key, name varchar)");
    }

    //create
    public void save(Member member) {
        template.update("insert into member(member_id, name) values(?, ?)",
            member.getMemberId(),
            member.getName());
    }

    // read
    public Member find(String memberId) {
        return template.queryForObject("select member_id, name from member where member_id =?", BeanPropertyRowMapper.newInstance(Member.class), memberId);
    }

    // update
    public void update(Member member) {
        template.update("update member set name =? where member_id =?",
            member.getName(),
            member.getMemberId());
    }

    // delete
    public void delete(String memberId) {
        template.update("delete from member where member_id =?", memberId);
    }

    // 전체 조회, List로 출력
    public List<Member> findAll() {
        return template.query("select member_id, name from member", BeanPropertyRowMapper.newInstance(Member.class));
    }


}
