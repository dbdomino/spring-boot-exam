package com.dbjdbc.jdbc.excption.translator;

import com.dbjdbc.domain.Member;
import com.dbjdbc.exception.MyDbException;
import com.dbjdbc.exception.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.dbjdbc.jdbc.connection.ConnectionConst.*;
import static org.springframework.jdbc.support.JdbcUtils.closeConnection;
import static org.springframework.jdbc.support.JdbcUtils.closeStatement;

/**
 * db의 에러코드를 읽고 거기 맞춰 예외에 대한 처리를 리포지터리/서비스 측에 구현해두는 것 테스트
 * db에서 반환하는 에러코드 읽어서 거기맞춰 로직 짜두는게 핵심.
 * 문제점, db종류마다 에러코드 반환하는게 다름, catch에서 에러코드 읽는방법이 다름.
 * 해결하기 위해 스프링 예외 추상화를 이용하자.
 */
public class ExTranslatorV1Test {

// duplicatException 테스트, id가 중복일때 id 변경해서 등록되도록 처리하는 로직 만들기. 요구조건이니깐 걍 해라
    // 테스트를위해 내부클래스로 구현.
    Repository repository;
    Service service;

    @BeforeEach
    void init() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave() {
        service.create("mymy");
        service.create("mymy"); // 같은아이디 저장시도.

    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service {
        private final Repository repository;

        public void create(String memberId){
            try {
                repository.save(new Member(memberId, 0));
                log.info("saveId={}", memberId);
            } catch (MyDuplicateKeyException e) {
                log.info("키 중복, 복구 시도");
                String retryId = generateNewId(memberId);
                log.info("retryId={}", retryId);
                repository.save(new Member(retryId, 0));
            } catch (MyDbException e) {
                log.info("데이터 접근 계층 예외", e);
                throw e;
            }
        }

        // 키 중복시 요청Id에 임의값붙여서 만듬.
        private String generateNewId(String memberId) {
            return memberId + new Random().nextInt(10000);
        }
    }

    @RequiredArgsConstructor
    static class Repository {
        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member2(member_id, money) values(?, ?)";
            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            } catch (SQLException e) {
                // e에서 db에서 쏴주는 에러코드를 어떻게 확인하는가? 가 핵심이네.  postgre는 e.getSQLState()
                 //h2 db postgre db  23505
                System.out.println("확인1 "+e.getSQLState()); // 에러코드가 String으로 나옴.
                System.out.println("확인1 "+e); // 에러코드가 0으로 나옴.
                System.out.println("확인1 "+e.getErrorCode()); // 에러코드가 0으로 나옴.
                if (e.getSQLState().equals("23505")) {
                    System.out.println("확인2 "+e.getErrorCode());
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);
            } finally {
                closeStatement(pstmt); // JdbcUtils 도구
                closeConnection(con);
            }
        }

    }

}
