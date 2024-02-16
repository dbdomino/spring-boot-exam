package com.minod.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.slf4j.Logger; 를 쓴다 중요
@RestController
public class LogTestController {
    private final Logger log = LoggerFactory.getLogger(LogTestController.class);

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name= "+name);
        /* 로그 레벨 설정   레벨이 info라면 제일 하위부터 지정된 info까지 출력된다.
        상위 trace
            debug
            info
            warn
        하위 error

        시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다.
        특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다.
        성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등등) 그래서 실무에서는 꼭 로그를 사용해야 한다.

         */
        log.trace(" 로그인포 trace log={} {} ", name,"미노듶vvv");  // slf4j 사용방법
        log.debug(" 로그인포 debug log={} {} ", name,"미노듶vvv");  // slf4j 사용방법
        log.info(" 로그인포 info log={} ", name);  // slf4j 사용방법
        log.info(" 로그인포 info log= "+name);  // 이렇게 쓰면 + 연산이 일어나는데, 로깅이 안되더라도 +연산이 일어나므로 자원소모가 생긴다고한다. 로깅에서 +연산은 쓰지말자 왠만하면
        log.warn(" 로그인포 warn log={} ", name);  // slf4j 사용방법
        log.error(" 로그인포 error log={} ", name);  // slf4j 사용방법

        return "ok";
    }

}
