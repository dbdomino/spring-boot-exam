package com.minod.전파;

import com.minod.aws.SecretManagerConfig;
import com.minod.propagation.LogRepository;
import com.minod.propagation.MemberRepository;
import com.minod.propagation.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    @BeforeAll
    static void before() throws IOException { //  히야...DB설정 SecretManager에서 불러와 프로퍼티에 적용하려면, 초기화 작업으로 이걸 해줘야 하네...
        System.out.println("start"); //
        SecretManagerConfig c = new SecretManagerConfig();
        c.mySecretProperty();
    }

    /**
     * MemberService    @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON
     */
    @Test
    void outerTxOff_success() {
        //given
        String username = "outerTxOff_success";
        //when
        memberService.joinV1(username);
//        memberService.joinV2(username);
        //then: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    @Test
    void outerTxOff_logging_fail() {
        //given
        String username = "outerTxOff_logging_fail";
        String username2 = "로그예외";
        //when
//        memberService.joinV1(username);
        memberService.joinV2(username2); // 예외발생
        //then: 멤버 쪽만 저장된다.
        assertTrue(memberRepository.find(username2).isPresent());
        assertFalse(logRepository.find(username2).isPresent());
        assertTrue(logRepository.find(username2).isEmpty());
    }

    /**
     * MemberService    @Transactional:On
     * MemberRepository @Transactional:OFF
     * LogRepository    @Transactional:OFF
     */
    @Test
    @Transactional
    void outerTxServiceOn_success() {
        //given
        String username = "outerTxServiceOn_success";
        //when
        memberService.joinV3(username); // 리포지터리에  @Transactional 빠짐.
        //then:
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService    @Transactional:On
     * MemberRepository @Transactional:On
     * LogRepository    @Transactional:On
     */
    @Test
    @Transactional
    void outerTxAllOn_success() {
        //given
        String username = "outerTxAllOn_success";
        //when
        memberService.joinV1(username); // 리포지터리에  @Transactional 각각있음. 서비스에도 @Transactional있음.
        //then: // 이때 먼저 실행된 outerTxAllOn_success 여기서 부터 트랜잭션이 있으므로, 커넥션도 공유하고 , 다른트랜잭션들은 참여(포함)된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON Exception
     */

    @Test
    @Transactional
    void outerTxAllOn_logging_fail() { // 포함된 트랜잭션중 하나라도 런타임 에러가 발생한다면? 전체롤백!, (런타임 에러 기준임.)   (체크드 예외는 커밋임)
        //given
        String username = "outerTxAllOn_logging_fail";
        String username2 = "로그예외";
        //when
//        memberService.joinV1(username);
        memberService.joinV2(username2); // 예외발생
//        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV2(username2)).isInstanceOf(UnexpectedRollbackException.class);
        log.info("--- joinV2 호출 종료");
        // joinV1이라면, 예외가 여기까지 던져지므로 여기서 런타임 에러로 잡히면서 예외 발생으로 종료되겠지.

        // 다만, 내부 트랜잭션에서 런타임에러로 던져지면서 rollbackOnly되었음. JoinV2에서 예외처리 하며 끝남
        // 내부 트랜잭션은 둘다 끝났으나, 여기 트랜잭션은 아직끝나지 않았으므로 물리 트랜잭션에 commit 되지 않음.
        // 그런데 여기서 조회가 발생했다?? 아래처럼 트랜잭션이 끝나기 전에 find 를 수행했다면, 영속성 컨텍스트에 있던 insert들이 물리 트랜잭션에 수행되면서, 값이 조회가 된다...
        // 다만 이 트랜잭션 끝나면 롤백되면서 데이터 사라지긴한데, 왜 find가 실행된다면, 물리트랜잭션에 insert가 수행되는 것인가?

        // 중요, 포함된 트랜잭션이 롤백되는 것이 핵심이며, 중간 과정, 즉 트랜잭션이 계속 되는 한, 예외가 잡혔기 때문에, 트랜잭션이 끝나기 전 까지는 rollback()이 물리적으로 일어나지 않은 상태이다.
        // 만약 select를 날린다면, rollback되지 않은 상태이므로 그 사이에 입력된 값은 조회가 되어야 하는게 맞다. 그거 지원하려고 트랜잭션매니저에서 flush()가 일어나고, 영속성 컨택스트에 대기중인 Insert문이 물리트랜잭션으로 전달되어 값이 조회가 되는 것이다.
        // 근데 이해가 안가는건 logRepository의 persist는 실패했을 것이다.그래서 런타임예외 터진건데, 왜 logRepository.find가 정상적으로 수행되고 조회도 되는 것일까? insert가 일어나면 안될 것인데,,
        System.out.println("겟겟 memberRepository "+memberRepository.find(username2).get().getUsername());
        System.out.println("겟겟 logRepository "+logRepository.find(username2).get().getMessage());
//        assertTrue(memberRepository.find(username2).isPresent());
//        assertTrue(logRepository.find(username2).isEmpty());

        // 결과적으로 여기메소드안의 작업은 전부 롤백은 된다.
    }


    /**
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON Exception (REQUIRES_NEW)
     */

    @Test
    void outerTxAllOn_Recover_before() { //LogRepository 의 save에  @Transactional(propagation = Propagation.REQUIRES_NEW) 를 적용한 메소드를 추가하여 테스트 진행
        //given
        String username = "outerTxAllOn_logging_fail";
        String username2 = "로그예외";
        //when
//        memberService.joinV4(username2); // 예외발생 .UnexpectedRollbackException: Transaction silently rolled back because it has been marked as rollback-only
        // 서비스에서 트랜잭션 종료되면서, rollbackOnly 잡혀있어commit실패로 런타임 에러 던져짐.
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV4(username2))
                .isInstanceOf(UnexpectedRollbackException.class);

        //then

//        System.out.println("겟겟 logRepository "+logRepository.find(username2).get().getMessage()); // outerTxAllOn_logging_fail 위와는 다르게 이거 조회 안됨. 오오...
        assertTrue(memberRepository.find(username2).isPresent());
        assertTrue(logRepository.find(username2).isEmpty());

        // 결과적으로 여기메소드안의 작업은 전부 롤백은 된다.
    }

    @Test
    void outerTxAllOn_Recover() { //LogRepository 의 save에  @Transactional(propagation = Propagation.REQUIRES_NEW) 를 적용한 메소드를 추가하여 테스트 진행
        //given
        String username = "outerTxAllOn_logging_fail";
        String username2 = "로그예외";
        //when
        memberService.joinV5(username2); // 예외발생 // 예외발생 가능성 있는 트랜잭션을 시작부터 Propagation.REQUIRES_NEW 로 분리해서 수행함. 예외터지더라도, Member리포지터리 작업이 롤백되는 일은 없다.
        log.info("--- joinV5 호출 종료");

        //then
//        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV5(username2))
//                .isInstanceOf(UnexpectedRollbackException.class);
//        System.out.println("겟겟 logRepository "+logRepository.find(username2).get().getMessage()); // outerTxAllOn_logging_fail 위와는 다르게 이거 조회 안됨. 오오...
        assertTrue(memberRepository.find(username2).isPresent());
        assertTrue(logRepository.find(username2).isEmpty());

        // 결과적으로 여기메소드안의 작업은 전부 롤백은 된다.
    }

}