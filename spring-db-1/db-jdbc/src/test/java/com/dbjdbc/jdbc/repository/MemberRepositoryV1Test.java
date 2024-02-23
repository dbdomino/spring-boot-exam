package com.dbjdbc.jdbc.repository;

import com.dbjdbc.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.dbjdbc.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;

    @BeforeEach()
    void before()  throws Exception {
        // 기본 DriverManage가 항상 새로운 커넥션을 획득하도록 하는
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        // 변경 Hicari CP 사용하기
        HikariDataSource dataSource = new HikariDataSource(); // 값을 넣기위해 구현체로, 리포지터리 내부에선 인터페이스로 구현체가 무엇인지 모르고 주입받아서 사용하도록 구현해야함.
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repositoryV1 = new MemberRepositoryV1(dataSource);
    }
    @Test
    void crud() throws SQLException, InterruptedException {
        log.info("start");

        Member member = new Member("memberV2", 10000);
        repositoryV1.save(member);

        //findById
        Member memberById = repositoryV1.findById(member.getMemberId());
        assertThat(memberById).isNotNull();

        //update: money: 10000 -> 20000
        repositoryV1.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryV1.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repositoryV1.delete(member.getMemberId());
        assertThatThrownBy(() -> repositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
/*//save
        Member member = new Member("memberV0", 10000);
        repositoryV1.save(member);
//findById
        Member memberById = repositoryV1.findById(member.getMemberId());
        assertThat(memberById).isNotNull();
//update: money: 10000 -> 20000
        repositoryV1.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryV1.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
//delete
        repositoryV1.delete(member.getMemberId());
        assertThatThrownBy(() -> repositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);*/
    }

}