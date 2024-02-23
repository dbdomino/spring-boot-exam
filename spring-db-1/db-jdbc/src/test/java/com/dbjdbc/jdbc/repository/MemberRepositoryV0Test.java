package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {
    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    @DisplayName("insert member 테스트")
    void crud() throws SQLException {
        //save
        Member member = new Member("membermobi", 50000);
        Member member1 = repository.save(member);

        System.out.println(member1);
        //delete
        repository.delete(member.getMemberId());
    }

    @Test
    @DisplayName("select member 테스트")
    void crudR() throws SQLException {
        //findById
        Member findMember = repository.findById("memberV0");
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(findMember);
    }

    @Test
    @DisplayName("Update member 테스트")
    void crudU() throws SQLException {
        //findById
        Member findMember = repository.findById("memberV0");
        //update: money: 10000 -> 20000
        repository.update(findMember.getMemberId(), 120000);

        //findById
        Member findMember2 = repository.findById("memberV0");
        log.info("findMember={}", findMember);
        log.info("findMemberAfter={}", findMember2);

        assertThat(findMember.getMoney()).isNotEqualTo(findMember2.getMoney());
    }

    @Test
    @DisplayName("Delete member 테스트")
    void crudD() throws SQLException {
        //save
        Member member = new Member("memVdude", 10000);
        repository.save(member);

        //delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class); // 삭제후 조회시 NoSuchElementException 이 나올거다. 이러면 성공 이라는 뜻.
    }

}