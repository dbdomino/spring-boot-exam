package com.dbjdbc.jdbc.repository.checked;

import com.dbjdbc.domain.Member;

import java.sql.SQLException;

public interface MemberRepositoryEx {
//    Member save(Member member) ; // 컴파일 에러, 체크드 에러인 SQLException는 구현체에 적으려면 인터페이스에도 선언되어야한다. 선언안되어어있어 컴파일에러
    Member save(Member member) throws SQLException;
//    Member findById(String memberId) ;  // 컴파일에러
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}