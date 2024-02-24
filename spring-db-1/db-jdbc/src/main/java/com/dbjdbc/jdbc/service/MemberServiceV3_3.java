package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * AOP 기능으로 트랜잭션 지원,
 * @Transactional 쓰면 AOP로 자동으로 트랜잭션 보장을 위한 소스를 집어넣어줌.
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_3 {
    // dataSource -> 트랜잭션 매니저 주입 -> 트랜잭션 탬플릿 주입. -> AOP로 트랜잭션 프록시 주입

    private final MemberRepositoryV3 memberRepository; // 트랜잭션을 위한 다른주입이 필요없다.

    @Transactional  // 이게 핵심.
    /**  @Transactional을 쓰면 스프링이 이 서비스 로직을 상속받는다.
     * 상속 받은 코드를 조작해서 기존에 사용하던 트랜잭션을 위한 소스를 만들어 준다고 함. try { ... transactionManager.commit(status) } catch { transactionManager.rollback (status)} ...
     * 소스에는 메소드내에 들어있는 accountBiz도 읽어서 포함시켜 준다고 함.
     */
    public void accountTransfer (String fromId, String toId, int money) throws SQLException{
            //비즈니스 로직
            accountBiz(fromId, toId, money);
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
