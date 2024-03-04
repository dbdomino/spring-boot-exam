package com.minod.advanced.logtracer;

import java.util.UUID;
// 로그 추적기는 트랜잭션ID와 깊이를 표현하는 방법이 필요하다. 이 기능을 담당함.
public class TraceId {
    private String id;
    private int level;

    public TraceId () {
        this.id = createId();
        this.level = 0;
    }
    private TraceId (String id, int level) {
        this.id = id;
        this.level = level;
    }

    /** 로그추적기 필요 메서드
     * createId       id생성기, uuid 기반
     * createNextId()        다음 TraceId 를 만든다 Id는 같고 level만 증가,
     *      createNextId() 를 사용해서 현재 TraceId 를 기반으로 다음 TraceId 를 만들면 id 는 기존과 같고, * level 은 하나 증가한다.
     * createPreviousId()    이전 TraceId 를 만든다 id 는 기존과 같고, level 은 하나 감소한다.
     * isFirstLevel()   첫 번째 레벨 여부를 편리하게 확인할 수 있는 메서드
     * getId()
     * getLevel()
     */

    private String createId() {
//        return UUID.randomUUID();
        return UUID.randomUUID().toString().substring(0,9); // 중복이 될 수 있지만, 구분용으로는 괜찮다.
    }
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }
    public boolean isFirstLevel() {
        return level == 0;
    }
    public String getId() {
        return id;
    }
    public int getLevel() {
        return level;
    }

}
