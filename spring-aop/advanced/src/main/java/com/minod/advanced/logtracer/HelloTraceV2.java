package com.minod.advanced.logtracer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**  로깅 클래스 구상 순서
 * 0. 요구사항 정리.
 * 1. 주요 동작 구성 및 메소드로 분리
 * 2. 메서드들 구현 상세화
 * 3. 필요한 상수 구성
 * 4. 실행 서비스에 메서드 조립
 */
@Slf4j
@Component // : 싱글톤으로 사용하기 위해 스프링 빈으로 등록
public class HelloTraceV2 {
    // 이거 의미가 좀 이해안간다.
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        // 로그출력 TraceStatus 객체를 만들어 보낸다.
        return new TraceStatus(traceId, startTimeMs, message);

    }

    /** 주요 메서드
     *     public TraceStatus begin(String message); 실행? TraceStatus 시작 로그를 시작한다.
     *     public void end(TraceStatus status);   로그를 정상 종료한다.
     *     public void exception(TraceStatus status, Exception e);   로그를 예외 상황으로 종료한다.
     *     private void complete(TraceStatus status, Exception e);   end(), exception(), 요청 흐름을 한곳에서 편리하게 처리한다. 실행 시간을 측정하고 로그를 남긴
     *     private static String addSpace(String prefix, int level); 레벨에 따라 prefix 반복출력
     *     V2 추가
     *     public TraceStatus beginSync(TraceId beforeTraceId, String message)  //깊이를 표현하는 Level로 prefix 같이 출력
     */
    public TraceStatus beginSync(TraceId beforeTraceId, String message){
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);

        return new TraceStatus(nextId, startTimeMs, message); // 다음 트레이스ID 넣은 Status 반환
    }

    public void end(TraceStatus status) {
        complete(status, null);
    }
    public void exception(TraceStatus status, Exception e) {
        complete(status, e); // exception터지면, 종료되나봄
    }
    private void complete(TraceStatus status, Exception e) {
        TraceId traceId = status.getTraceId();
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();

        if( e == null ) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
    }
    private static String addSpace(String prefix, int level) { // 핵심메서드, prefix랑 level을 조합해줌.
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<level;i++){ // level로 띄워쓰기를 통해 깊이 표현
            sb.append( (i==level-1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

    //

}
