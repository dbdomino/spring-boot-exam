package com.minod;

import com.minod.aws.CustomApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TransactionApplication.class);
		application.addInitializers(new CustomApplicationContextInitializer()); // 초기화작업으로 프로퍼티 추가
		application.run(args);
	}

}
