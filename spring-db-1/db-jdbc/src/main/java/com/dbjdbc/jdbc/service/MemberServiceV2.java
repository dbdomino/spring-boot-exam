package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    // dataSource를 받아와야한다, Connection을 얻어오기 위해서. 왜? 리포지터리 명령에 connection도 주입해서 하나의 커넥션을 공유해서 쓰기위해
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer (String fromId, String toId, int money) throws SQLException{
        Connection con = dataSource.getConnection();

        try {
            con.setAutoCommit(false); // 트랜잭션 시작

            /* 비즈니스 로직 분리  ctrl+alt+m
            Member fromMember = memberRepository.findById(fromId);
            Member toMember = memberRepository.findById(toId);

            memberRepository.update(fromId, fromMember.getMoney()-money);
            validation(toMember);
            memberRepository.update(toId, toMember.getMoney()+money);
            */
            accountBiz(con,fromId, toId, money);

            con.commit(); // 성공시 커밋

        } catch (Exception e) {
            con.rollback(); // 실패시 롤백
            throw new IllegalStateException(e); // 에러 밖으로 던지기 걍 이렇게해줌.
        } finally {
            conRelease(con);
            // r긴것도 있지만 비즈니스로직과 트랜잭션 관리로직을 분리하기 위해서다.
/*  이거도 기니까 분리  ctrl + alt + m
                if (con != null) {
                try {
                    con.setAutoCommit(false); // 트랜잭션 종료, 성공이든 실패든 원래데로 해서 돌려줘야함.
                    con.close();
                } catch (SQLException ex) {
                    log.debug("Could not close JDBC Connection", ex);
                } catch (Exception e) { // 익셉션 로그 남길 때 log.info("error {}",e); 이렇게 안하고 아래처럼 한단다. 왜?
                    // 예외정보는 너무길고 복잡하므로, stacktrace를 출력하고 싶을 때만 e를 출력시키고, 그게아니면, 뒤에인자로 남겨두기만 한다.
                    // 사용자 정보룰 추가하고 싶다면 이렇게 작성한다. log.info("error {}",message,e);
                    log.info("error",e);
                }

            }*/
        }
    }

    private static void conRelease(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(false); // 트랜잭션 종료, 성공이든 실패든 원래데로 해서 돌려줘야함.
                con.close();
            } catch (SQLException ex) {
                log.debug("Could not close JDBC Connection", ex);
            } catch (Exception e) { // 익셉션 로그 남길 때 log.info("error {}",e); 이렇게 안하고 아래처럼 한단다. 왜?
                // 예외정보는 너무길고 복잡하므로, stacktrace를 출력하고 싶을 때만 e를 출력시키고, 그게아니면, 뒤에인자로 남겨두기만 한다.
                // 사용자 정보룰 추가하고 싶다면 이렇게 작성한다. log.info("error {}",message,e);
                log.info("error",e);
            }

        }
    }

    private void accountBiz(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con,fromId);
        Member toMember = memberRepository.findById(con,toId);

        memberRepository.update(con,fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(con,toId, toMember.getMoney()+ money);
    }

    // 계좌이체 예시 만들기 v1


    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외발생, 맴버ID ex는 입금금지");
        }
    }


}
