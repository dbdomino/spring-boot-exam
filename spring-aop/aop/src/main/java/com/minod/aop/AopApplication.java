package com.minod.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@Import(AspectV1.class)
//@Import(AspectV3.class)
//@Import(AspectV4.class) // 단순히 Bean 등록할 때 쓸 수도 있음, 컨피그파일로 빈 한번에 등록도 가능
//@Import({AspectV5.LogAspect.class, AspectV5.txAspect.class}) //
//@Import({AspectV6.class}) //
//@Import({OrderAopConfig.class}) //
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);


    }
}
