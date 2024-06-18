package com.hello.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
@Slf4j
@Configuration   //주석치면, dataSource() 설정이 스캔되지 않는다, 그럼 어떻게 될까?
// HikariPool-1 -  Added connection conn0: url=jdbc:h2:mem:9f0f9259-3a77-4bef-9758-21fb7dc5197e user=SA   이런식으로 된다.
// 프로퍼티에도 정보가 없다보니 알아서 H2 메모리 db로 등록해서 쓰게 된다.
public class DbConfig {
    // datasource 설정
    @Bean
    public DataSource dataSource() {
        log.info("DataSource    수동으로 빈 등록");
        HikariDataSource datasource = new HikariDataSource();
//        datasource.setDriverClassName("org.mariadb.jdbc.Driver");
//        datasource.setDriverClassName("org.h2.Driver");
        datasource.setDriverClassName("org.postgresql.Driver");
//        datasource.setJdbcUrl("jdbc:h2:mem:test"); // 메모리 DB
        datasource.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb");
        datasource.setUsername("minod");
        datasource.setPassword("minod");
        return datasource;
    }

    // transaction 매니저 tm 생성, 트랜잭션 생성 위해 dataSource 기준으로 생성함.
    // `@Transactional` 을 사용하려면 `TransactionManager` 가 스프링 빈으로 등록되어 있어야 한다.
    @Bean
    public TransactionManager transactionManager() {
        log.info("transactionManager 빈 등록");
        return new JdbcTransactionManager(dataSource());
    }

    // JDBC 탬플릿 직접 등록, 원하는 dataSource() 마다 등록하는게 가능함.
    @Bean
    public JdbcTemplate jdbcTemplate() {
        log.info("jdbcTemplate 빈 등록");
        return new JdbcTemplate(dataSource());
    }
}
