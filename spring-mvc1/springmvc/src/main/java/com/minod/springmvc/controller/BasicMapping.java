package com.minod.springmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicMapping {
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 기본 요청
     * 둘다 허용 /hello-basic, /hello-basic/
     * HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }
    /*
    @RestController
        @Controller 는 반환 값이 String 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
        @RestController 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다.
        따라서실행 결과로 ok 메세지를 받을 수 있다. @ResponseBody 와 관련이 있는데, 뒤에서 더 자세히 설명한다.

    @RequestMapping("/hello-basic")
        /hello-basic  URL 호출이 오면 이 메서드가 실행되도록 매핑한다.
        대부분의 속성을 배열[] 로 제공하므로 다중 설정이 가능하다. {"/hello-basic", "/hello-go"}

    / 슬러시 주의
    스프링 부트 3.0 부터는 /hello-basic , /hello-basic/ 는 서로 다른 URL 요청을 사용해야 한다.
        기존에는 마지막에 있는 / (slash)를 제거했지만, 스프링 부트 3.0 부터는 마지막의 / (slash)를 유지한다.
        따라서 다음과 같이 다르게 매핑해서 사용해야 한다.
        매핑: /hello-basic   URL 요청: /hello-basic
        매핑: /hello-basic/   URL 요청: /hello-basic/
     */
}
