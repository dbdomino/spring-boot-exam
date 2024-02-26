package com.dbjdbc.jdbc.excption.translator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.dbjdbc.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 예외 변환기 테스트, 스프링 예외 변환기
 * SQLExceptionTranslator
 *             SQLExceptionTranslator exTranslator = new  SQLErrorCodeSQLExceptionTranslator(dataSource);
 */
@Slf4j
public class SpringExceptionTranslatorTest {
    DataSource dataSource;
    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammar"; // 쿼리 이상할 때도
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            assertThat(e.getSQLState()).isEqualTo("42703");
            int errorCode = e.getErrorCode();
            log.info("errorCode={}", errorCode);
            //org.h2.jdbc.JdbcSQLSyntaxErrorException
            log.info("error", e);
        }
    }

    @Test
    void exceptionTranslator() {
        String sql = "select bad grammar";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            // SQLException e 에서 포스트그리 에러코드를 반환하는건 e.getErrorCode() 가 아니라 e.getSQLState() 이다.

//            System.out.println(e.getErrorCode());
//            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());  // 당첨
//            System.out.println(e.getCause());
            assertThat(e.getSQLState()).isEqualTo("42703");

            // 핵심. 스프링 예외 변환기 (예외 변환기가 bad SQL grammar 라고 출력함.)
//org.springframework.jdbc.support.sql-error-codes.xml
            SQLExceptionTranslator exTranslator = new  SQLErrorCodeSQLExceptionTranslator(dataSource);
//org.springframework.jdbc.BadSqlGrammarException
            DataAccessException resultEx = exTranslator.translate("select", sql, e);
            log.info("resultEx", resultEx); // 스택트레이스 출력,
            // select; bad SQL grammar [select bad grammar]
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }
}
