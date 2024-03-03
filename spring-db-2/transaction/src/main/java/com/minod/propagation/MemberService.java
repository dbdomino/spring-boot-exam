package com.minod.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 저장 서비스
 * - 회원저장, 로그저장 같이함
 * - 회원저장 후 로그저장 실패날 경우 예외처리, 로그를 파일로 서버저장. 또는 콘솔로 남김
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    public void joinV1(String username) {
        Member member = new Member(username);
        LogFactory logFactory = new LogFactory(username); // logFactory 객체 만드는이유? logFactory객체에 (username 정보) 저장하려고.

        log.info("--- memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("--- memberRepository 호출 종료");
        log.info("===--- logRepository 호출 시작");
        logRepository.save(logFactory);
        log.info("===--- logRepository 호출 종료");
    }

    public void joinV2(String username) { // 로그 저장 실패하면, throw 되는건 피하기 위해
        Member member = new Member(username);
        LogFactory logFactory = new LogFactory(username); // logFactory 객체 만드는이유? logFactory객체에 (username 정보) 저장하려고.

        log.info("--- memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("--- memberRepository 호출 종료");

        try { // 로그저장 실패한다해도 예외 안던지기 위해 직접처리
            log.info("===--- logRepository 호출 시작");
            logRepository.save(logFactory);
            log.info("===--- logRepository 호출 종료");
        } catch (RuntimeException e) {
            log.info("로그 저장 실패, logRepository={}",logFactory.getMessage());
            log.info("로그 저장 실패, ErrorMessage={}",e.getMessage());
        }
        log.info("--- joinV2 종료");
    }

    public void joinV3(String username) {
        Member member = new Member(username);
        LogFactory logFactory = new LogFactory(username); // logFactory 객체 만드는이유? logFactory객체에 (username 정보) 저장하려고.

        log.info("--- memberRepository 호출 시작");
        memberRepository.saveNoTransactional(member);
        log.info("--- memberRepository 호출 종료");
        log.info("===--- logRepository 호출 시작");
        logRepository.saveNoTransactional(logFactory);
        log.info("===--- logRepository 호출 종료");
    }

    @Transactional
    public void joinV4(String username) { // 로그 저장 실패하면, throw 되는건 피하기 위해
        Member member = new Member(username);
        LogFactory logFactory = new LogFactory(username); // logFactory 객체 만드는이유? logFactory객체에 (username 정보) 저장하려고.

        log.info("--- memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("--- memberRepository 호출 종료");

        try { // 로그저장 실패한다해도 예외 안던지기 위해 직접처리
            log.info("===--- logRepository 호출 시작");
            logRepository.save(logFactory);
            log.info("===--- logRepository 호출 종료");
        } catch (RuntimeException e) {
            log.info("로그 저장 실패, logRepository={}",logFactory.getMessage());
            log.info("로그 저장 실패, ErrorMessage={}",e.getMessage());
        }
        log.info("--- joinV4 종료");
    }

    @Transactional
    public void joinV5(String username) { // 로그 저장 실패하면, throw 되는건 피하기 위해
        Member member = new Member(username);
        LogFactory logFactory = new LogFactory(username); // logFactory 객체 만드는이유? logFactory객체에 (username 정보) 저장하려고.

        log.info("--- memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("--- memberRepository 호출 종료");

        try { // 로그저장 실패한다해도 예외 안던지기 위해 직접처리
            log.info("===--- logRepository 호출 시작");
            logRepository.saveREQUIRES_NEW(logFactory);
            log.info("===--- logRepository 호출 종료");
        } catch (RuntimeException e) {
            log.info("로그 저장 실패, logRepository={}",logFactory.getMessage());
            log.info("로그 저장 실패, ErrorMessage={}",e.getMessage());
        }
        log.info("--- joinV4 종료");
    }
}
