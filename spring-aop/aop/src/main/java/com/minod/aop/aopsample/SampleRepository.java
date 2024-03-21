package com.minod.aop.aopsample;

import org.springframework.stereotype.Repository;

@Repository
public class SampleRepository {
    private static int seq = 0; // 실제로 이런식으로 쓰면 위험한데..
    // 이런식으로 공통적으로 필요한 상태값은 싱글턴에서 관리하면 정합성 문제 백퍼 생긴다.
    // 동시성 문제 라고 함. 원하지 않는 결과가 나옴.
    // 스레드, 작업 단위마다 공통적으로 바라보는 값일 경우 동기화를 지원할 수 있어야 한다.
    // 동기화를 지원하기 위한 컬렉션이나 변수를 알아두자. 비동기로 여러 스래드 쓸거면, ConcurrerntHashMap 같은 것들도 고려해야함

    // 시퀀스가 5배수일때 실패하는 요청, 즉 5번중 한번 실패
    public String save(String itemId) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }

        return "save success";
    }

}
