package com.minod.proxy;

import com.minod.proxy.config.v6_aop.AopConfig;
import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.LogTracerThreadLocal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import(AppV1Config.class)
//@Import(AppV2Config.class)
//@Import(AppV3Config.class)
//@Import({AppV1Config.class, AppV2Config.class, AppV3Config.class}) // @Import 안에 배열로 등록하고 싶은 설정파일을 다양하게 추가할 수 있다.
//@Import({InterfaceProxyConfig.class, AppV2Config.class})
//@Import({ConcreteProxyConfig.class})
//@Import({DynamicProxyBasicConfig.class, AppV2Config.class})
//@Import({DynamicProxyNologConfig.class, AppV2Config.class})
//@Import({ProxyFactoryConfigV1.class, AppV2Config.class})
//@Import({ProxyFactoryConfigV2.class})
//@Import({BeanPostProcessorConfig.class})
//@Import({AutoProxyCreatorConfig.class})
@Import({AopConfig.class})
@SpringBootApplication(scanBasePackages = "com.minod.proxy.app") // 여기 지정된 경로에서만 스캔한다는 소리
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
	@Bean // 일딴 추가해봄.
	public LogTracer logTracer() {
		return new LogTracerThreadLocal(); // 동시성 문제 해결시킨 쓰레드 로컬 이용
		// 패턴? 여기서 가 아니라, 프록시에서 반복으로 로그 추적기 소스 들어갔기 때문. 보면 전략패턴으로 들어감. (프록시 객체 생성할 때 로그 추적기가 들어감)
	}
}
