package com.minod;

import com.minod.aws.CustomApplicationContextInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class TransactionApplicationTests {

	@Test
	void contextLoads() {
		// 테스트에서 addInitializer 수행되도록 하기
		SpringApplication application = new SpringApplication(TransactionApplication.class);
		application.addInitializers(new CustomApplicationContextInitializer());  // 초기화작업으로 시크릿매니저 값 프로퍼티에 추가
		ConfigurableApplicationContext context = application.run();
		// 테스트 내용 추가
		context.close(); // 테스트 종료 후 애플리케이션 컨텍스트 닫기
	}

}
