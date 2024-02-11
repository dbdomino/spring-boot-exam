package com.minod.core.controller;

import com.minod.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    // 두개 의존성주입해서 사용할거임.
    private final LogDemoService logDemoService;
//    private final MyLogger myLogger;   // request Scope 이다. Bean이 Request Scope라서 요청이 들어와서 나갈때 까지만 Bean이 살아있다. 스프링 띄울때는 request가 없으므로 스프링 시작이 안된다.
    // Error creating bean with name 'myLogger': Scope 'request' is not active for the current thread; myLogger를 request Scope로 쓸려니까 생기는 오류
    // consider defining a scoped proxy for this bean if you intend to refer to it from a singleton; 싱글턴 Scope 으로 바꿔서 쓰라고 한다. 내가봐도 이게맞다. 로거니까.
    // 스프링 인터셉터나 서블릿 필터 같은 곳을 활용하여 공통로직으로 처리하는 것도 좋다.
    // request scope로 사용하려면, 의존관계 주입단계에서 Bean을 주입하는게 아니라 고객이 요청했을 때로 미뤄야한다. (Provider로 가능) 지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 제공하는 것이 바로 ObjectProvider 이다
    // ObjectFactory: 기능이 단순, 별도의 라이브러리 필요 없음, 스프링에 의존
    // ObjectProvider: ObjectFactory 상속, 옵션, 스트림 처리등 편의 기능이 많고, 별도의 라이브러리 필요 없음, 스프링에 의존
    // Provider 쓰려면 Controller Service 다고쳐야한다. Mylogger 쓰는곳 전부다 고쳐야한다. 귀찮다.
//    private final MyLogger myLogger; // Mylogger 바로 주입하기보다 ObjectProvider로 주입하는 것이에용. Optional 로 감싸 주입하는 방식과 비슷하다.
//    private final ObjectProvider<MyLogger> myLoggerProvider;

    // MyLogger 에 프록시 스코프로 설정한다면, 의존관계 주입시 가짜 프록시 객체가 주입됨. 가짜 프록시 객체는 실제 요청이 오면, 그제서야 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
    // 진짜 객체를 가짜프록시가 찾아오는 방법이 Proxy 객체에 들어있다,.
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
//        MyLogger myLogger = myLoggerProvider.getObject(); // getObject()를 logDemo가 실행되는 시점으로 바꾼다.
        // 마이크로서비스 에서 아이디를 서비스 넘길 때 requestScope로 같이 넘기고, 같이 로그를 남겨주면, 같은 로그를 한번에 묶여서 볼 수 있다.
        // 핵심, 동시에 여러 요청이 온다해도, 요청마다 MyLogger 객체가 생기고 각각 관리해준다.

        StringBuffer requestUrl = request.getRequestURL();  // 이건 StringBuffer 로 반환하네.
        String requestUri = request.getRequestURI();  // 이건 에러안나고

        myLogger.setRequestURL("URL이야 "+ requestUrl.toString()); // 얘는 실행안됨.
        myLogger.setRequestURL("URI이야 "+requestUri);
        System.out.println("LogDemoController.logDemo");
        Thread.sleep(1000);

        logDemoService.logic("testId");
        return "OK"; // 객체로 반환도 가능
    }
}
