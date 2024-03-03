package com.minod;

import com.minod.aws.CustomApplicationContextInitializer;

//@SpringBootApplication
public class SpringApplication {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication application;
        application = new org.springframework.boot.SpringApplication(SpringApplication.class);
        application.addInitializers(new CustomApplicationContextInitializer()); // 초기화작업으로 프로퍼티 추가
		application.run(args);
	}

}
