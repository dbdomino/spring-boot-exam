package com.minod.core.singleton;

public class SingletonService {

    // 일반적인 싱글턴패턴 클래스
    private static final SingletonService instance = new SingletonService();
    // public으로 열어서 객체 인스턴스 필요하면 만들어진 static 메서드만 사용하도록 한다
    public static SingletonService getInstance() {
        return instance;
    }

    // public 을 private으로 바꿔 생성자 호출 막는다.
    private SingletonService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
