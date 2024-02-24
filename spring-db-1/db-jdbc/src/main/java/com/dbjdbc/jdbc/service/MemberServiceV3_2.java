package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 탬플릿(TransactionTemplate) - 트랜잭션 매니저 받아와 사용
 * 트랜잭션 탬플릿으로 try catch 반복으로 commit, rollback 소스 제거
 */

@Slf4j
//@RequiredArgsConstructor // 트랜잭션 탬플릿은 생성자 주입으로 받기 위해 커스터마이징 필요.
public class MemberServiceV3_2 {
    // dataSource -> 트랜잭션 매니저 주입 -> 트랜잭션 탬플릿 주입.

//    private final PlatformTransactionManager transactionManager;  // 핵심, 트랜잭션 매니저, 얘도 주입해서 사용(JDBC 트랜잭션매니저, JPA 트랜잭션매니저 골라서 주입가능
    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    // 트랜잭션 매니저를 주입해서 트랜잭션 탬플릿을 만들어야 한다. 원래 이런식이라 쓸려면 같이 써야 한다.
    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer (String fromId, String toId, int money) throws SQLException{
        // 트랜잭션 매니저 안씀
//        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // 트랜잭션 탬플릿으로 executeWithoutResult 쓰면 내부에 try, catch로 commit, rollback 다 들어 있다고 함.
        // 한계점으로 언체크드 익셉션은 롤백하는데, 체크드 익셉셧은 커밋된다. 체크드 익셉션은 별도로 exception날려준다.
        txTemplate.executeWithoutResult((status) -> {
            try {
            //비즈니스 로직
                accountBiz(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void accountBiz( String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney()+ money);
    }

    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외발생, 맴버ID ex는 입금금지");
        }
    }


}
