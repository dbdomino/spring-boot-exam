package com.minod.advanced.logtracer;

import lombok.extern.slf4j.Slf4j;

import static com.minod.advanced.logtracer.PREFIX.*;

/** 두번째 구현체, 테스트 ThreadLocalLogTraceTest
 * ThreadLocal에 TraceId를 저장하여 멀티 스래드 환경, 여러 요청이 동시에 들어올 때 발생하는 동시성 문제를 방지할 수 있다.
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
public class LogTracerThreadLocal implements LogTracer{
    // TraceId traceId = new TraceId(); // 기존에 객체 만들어 썼으나 이제 주입받아 쓴다고 함.
//    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생 가능성 생긴다고 함.
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>(); // 쓰레드 로컬로 동시성 이슈 해결, 직접구현이네

    @Override
    public TraceStatus begin(String message) {
//        TraceId traceId = new TraceId();
        syncTraceId(); // 맴버변수 traceIdHolder에다 TraceId 저장한다.
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", traceIdHolder.get().getId(), addSpace(START.getPrefix(), traceIdHolder.get().getLevel()), message);
        // 로그출력 TraceStatus 객체를 만들어 보낸다.
        return new TraceStatus(traceIdHolder.get(), startTimeMs, message);
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
        TraceId traceId = traceIdHolder.get(); // 지역변수는 동시성 문제에 영향이 없을 것이다.
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {

            traceIdHolder.set(traceIdHolder.get().createNextId());
        }
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
//            traceIdHolder.set(null); // null로 저장
            traceIdHolder.remove(); // 해당 쓰레드가 저장한 값이 사라짐. 모든 쓰레드가 저장한 값이 사라지진 않음.
        } else {
//            traceIdHolder.set(traceIdHolder.get().createPreviousId());
            traceIdHolder.set(traceId.createPreviousId());
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
