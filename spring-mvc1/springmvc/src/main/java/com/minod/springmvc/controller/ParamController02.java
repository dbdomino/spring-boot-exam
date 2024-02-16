package com.minod.springmvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ParamController02 {
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     * request.getParameter()
     * 여기서는 단순히 HttpServletRequest가 제공하는 방식으로 요청 파라미터를 조회했다.
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        System.out.println("ParamController02.requestParamV1");
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }
    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩
     * @ResponseBody 추가
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {
        System.out.println("ParamController02.requestParamV2");
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     * ("key이름") 생략할수있으며 파라미터명이 key이름과 일치해야한다.
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {
        System.out.println("ParamController02.requestParamV3");
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
     * String , int , Integer 등의 단순 타입이면 @RequestParam 도 생략 가능
     * @RequestParam 애노테이션을 생략하면 스프링 MVC는 내부에서 required=false 를 적용한다.
     *
     * 이렇게 애노테이션을 완전히 생략해도 되는데, 너무 없는 것도 약간 과하다는 생각도 든다.
     * @RequestParam 이 있으면 명확하게 요청 파리미터에서 데이터를 읽는 다는 것을 알 수 있다.
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(
            String username,
            int age) {
        System.out.println("ParamController02.requestParamV4");
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /*
    스프링 부트 3.2부터 자바 컴파일러에 -parameters 옵션을 넣어주어야 애노테이션에 적는 이름을 생략할 수 있다.
    주로 다음 두 애노테이션에서 문제가 발생한다. 나는 발생안하는데, 하는경우도있다고한다.
    Build and run using를 IntelliJ IDEA로 선택한 경우에만 발생한다.
    key를 넣어주는게 나을지도 모르겠다.
    @RequestParam , @PathVariable

    해결 방안1(권장)
    애노테이션에 이름을 생략하지 않고 다음과 같이 이름을 항상 적어준다. 이 방법을 권장한다.
    @RequestParam("username") String username
    @PathVariable("userId") String userId

    해결 방안2
컴파일 시점에 -parameters 옵션 적용
1.   IntelliJ IDEA에서 File    Settings를 연다. (Mac은 IntelliJ IDEA    Settings)
2.   Build, Execution, Deployment → Compiler → Java Compiler로 이동한다.
3.   Additional command line parameters라는 항목에 다음을 추가한다.
-parameters
4.   out 폴더를 삭제하고 다시 실행한다. 꼭 out 폴더를 삭제해야 다시 컴파일이 일어난다

    해결 방안3
    Gradle을 사용해서 빌드하고 실행한다.
     */

    //애노테이션에 이름이 없다. -parameters 옵션 필요
    @RequestMapping("/request31")
    public String request31(@RequestParam String username) {
        log.info("username={}, ", username);
        return "ok";
    }
    //애노테이션도 없고 이름도 없다. -parameters 옵션 필요
    @RequestMapping("/request32")
    public String request32(String username) {
        log.info("username={}, ", username);
        return "ok";
    }

}
