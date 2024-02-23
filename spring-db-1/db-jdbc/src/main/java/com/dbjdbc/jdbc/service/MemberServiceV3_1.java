package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/**
 * 트랜잭션 매니저
 *
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    // dataSource를 받아와야한다, Connection을 얻어오기 위해서. 왜? 리포지터리 명령에 connection도 주입해서 하나의 커넥션을 공유해서 쓰기위해
    // 트랜잭션 매니저를 쓰기위해 dataSource 안쓴다.
//    private final DataSource dataSource;

    private final PlatformTransactionManager transactionManager;  // 핵심, 트랜잭션 매니저, 얘도 주입해서 사용(JDBC 트랜잭션매니저, JPA 트랜잭션매니저 골라서 주입가능)
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer (String fromId, String toId, int money) throws SQLException{
//        Connection con = dataSource.getConnection();
        // 트랜잭션 시작. 커넥션을 넘길 필요가 없다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 비즈니스로직 분리
            accountBiz(fromId, toId, money);

//            con.commit(); // 성공시 커밋
            transactionManager.commit(status);

        } catch (Exception e) {
//            con.rollback(); // 실패시 롤백
            transactionManager.rollback(status);
            throw new IllegalStateException(e); // 에러 밖으로 던지기 걍 이렇게해줌.
        } /*finally { 트랜잭션 매니저가 commit이나 rollback될때 ReleaseCon 해준다고함. 그래서 생략
            conRelease(con);
        }*/
    }

    private void accountBiz( String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney()+ money);
    }

    // 계좌이체 예시 만들기 v1


    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외발생, 맴버ID ex는 입금금지");
        }
    }


}
