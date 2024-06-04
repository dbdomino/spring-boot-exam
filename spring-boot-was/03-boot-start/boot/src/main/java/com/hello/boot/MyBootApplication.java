package com.hello.boot;

import com.hello.boot.config.MySpringApplication;
import com.hello.boot.config.MySpringBootApplication;

@MySpringBootApplication
public class MyBootApplication {

    public static void main(String[] args) {
        System.out.println("MyBootApplication.main() 시작");
//        SpringApplication.run(MyBootApplication.class, args);
        MySpringApplication.run(MyBootApplication.class, args);
    }

}
