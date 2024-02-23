package com.dbjdbc.connection;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test {
    String dbDriver = "org.postgresql.Driver";
    public final String POSTGRE_URL = "jdbc:postgresql://localhost:5432/book";
    public final String USERNAME = "minod";
    public final String PASSWORD = "minodminod";
    @Test
    void postgreTest() throws ClassNotFoundException {
        String driverName="org.postgresql.Driver";//포스트그리 드라이버 연결.

        Connection conn = null;
        try {
            Class.forName(dbDriver); //JDBC 드라이버 이름으로 드라이버를 로드함.//    conn=DriverManager.getConnection(url,"아이디 자리","비밀번호 자리");
            conn = DriverManager.getConnection(POSTGRE_URL, USERNAME, PASSWORD);
            System.out.println("DB Connection [성공]");
            conn.close();    //데이터베이스와의 연결을 해제함}
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
