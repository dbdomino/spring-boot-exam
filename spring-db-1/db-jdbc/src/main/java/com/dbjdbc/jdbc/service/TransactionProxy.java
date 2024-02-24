package com.dbjdbc.jdbc.service;

// 트랜잭션 프록시
// 프록시를 사용하면, 트랜잭션 프록시가, 트랜잭션 처리를 위한 코드를 아래처럼 동적으로 만들어 낸다고 함.
// 조건은 @Transactional이 붙은 메소드가 있을 때 라고한다.
public class TransactionProxy {
/*    private MemberService target;
    public void logic() {
    //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(..);
        try {
    //실제 대상 호출
            target.logic();
            transactionManager.commit(status); //성공시 커밋
        } catch (Exception e) {
            transactionManager.rollback(status); //실패시 롤백
            throw new IllegalStateException(e);
        }
    }*/
}