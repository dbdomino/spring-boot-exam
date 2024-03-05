package com.minod.proxy.logtracer;

import lombok.extern.slf4j.Slf4j;

import static com.minod.proxy.logtracer.PREFIX.*;

/** 첫번째 구현체
 * 파라미터를 넘기지 않고 TraceId를 동기화 할 수 있는 LogTracer 구현체
 주요 메서드
 *     인터페이스
 *     public TraceStatus begin(String message); 실행? TraceStatus 시작 로그를 시작한다.
 *     public void end(TraceStatus status);   로그를 정상 종료한다.
 *     public void exception(TraceStatus status, Exception e);   로그를 예외 상황으로 종료한다.
 *     구현체
 *     private void complete(TraceStatus status, Exception e);   end(), exception(), 요청 흐름을 한곳에서 편리하게 처리한다. 실행 시간을 측정하고 로그를 남긴
 *     private static String addSpace(String prefix, int level); 레벨에 따라 prefix 반복출력
 *     private void syncTraceId();     // TraceId를 동기화 하는 메서드
 *     private void releaseTraceId()  // complete() 끝날 때, TraceId 동기화 하는 메서드
 *
 */
@Slf4j
public class LogTracerField implements LogTracer{
    // TraceId traceId = new TraceId(); // 기존에 객체 만들어 썼으나 이제 주입받아 쓴다고 함.
    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생 가능성 생긴다고 함.

    @Override
    public TraceStatus begin(String message) {
//        TraceId traceId = new TraceId();
        syncTraceId(); // 맴버변수 traceIdHolder에다 TraceId 저장한다.
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", traceIdHolder.getId(), addSpace(START.getPrefix(), traceIdHolder.getLevel()), message);
        // 로그출력 TraceStatus 객체를 만들어 보낸다.
        return new TraceStatus(traceIdHolder, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    // -인터페이스 외의 메서드 추가 구현
    private void complete(TraceStatus status, Exception e) {
        TraceId traceId = status.getTraceId();
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();

        if( e == null ) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE.getPrefix(), traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX.getPrefix(), traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }

        releaseTraceId(); // 해당 메서드 끝났을 때 TraceId의 레벨 내리는 역할, complete()니까 필요.
    }

    private void syncTraceId() { // TraceId를 새로 생성할지, 기존걸 가져올지 정하는 메서드.
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    private void releaseTraceId() {
        if (traceIdHolder.isFirstLevel()) {
            traceIdHolder = null; // destroy
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) { // 핵심메서드, prefix랑 level을 조합해줌.
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<level;i++){ // level로 띄워쓰기를 통해 깊이 표현
            sb.append( (i==level-1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }


}
