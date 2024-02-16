package com.minod.springmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ParamController03 {
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * @RequestParam : 파라미터 이름으로 바인딩
     * @ResponseBody : return이 있을 때 "해당 viewResolver로 매핑시키지 않고,
     *          response Body에 그대로 값을 출력, 객체를 출력도 가능, -> @RestController를 쓰면 굳이 안써도되긴함.
     * @RequestParam.required
     * /request-param-required -> username이 없으므로 예외
     *
     * 주의!
     * /request-param-required?username= -> 빈문자로 통과
     *
     * 주의!
     * /request-param-required
     * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는
    defaultValue 사용)
     */

    /*
@RequestBody를 통해서 자바객체로 conversion을 하는데, 이때 HttpMessageConverter를 사용한다.
@ResponseBody 가 붙은 파라미터에는 HTTP 요청의 분문 body 부분이 그대로 전달된다.
RequestMappingHandlerAdpter 에는 HttpMessageConverter 타입의 메세지 변환기가 여러개 등록되어 있다.

@RequestBody
이 어노테이션이 붙은 파라미터에는 http요청의 본문(body)이 그대로 전달된다.
일반적인 GET/POST의 요청 파라미터라면 @RequestBody를 사용할 일이 없을 것이다.
반면에 xml이나 json기반의 메시지를 사용하는 요청의 경우에 이 방법이 매우 유용하다.
HTTP 요청의 바디내용을 통째로 자바객체로 변환해서 매핑된 메소드 파라미터로 전달해준다.
     */
    /**
    * http://localhost:8080/request-param-required?username=hello&age=20
     * required = true    일 경우 필수값이 된다.
     * int는 프리미티브 값으로 null이 될수 없기에 Integer로 받아서 쓰게한다.
    * */
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username, // username=   이것도 통과된다.. 빈값이면 빈값으로 출력된다.
            @RequestParam(required = false) Integer age) {  // age=     일 경우 null로 뜬다.
        if(username == null) log.info("username isnull");
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam
     * - defaultValue 사용
     *
     * 참고: defaultValue는 빈 문자의 경우에도 적용
     * /request-param-default?username=
     * 매우 좋은 기능   이미 기본 값이 있기 때문에 required 는 의미가 없다.
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username, //
            @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /** 파라미터를 맵으로 조회하기
     * 여러 파라미터들을 하나의 맵으로 받아오게해서 불러오는 것도 가능하다.
     * @RequestParam Map, MultiValueMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
    @ResponseBody
    @RequestMapping("/request-param-map2")
    public String requestParamMap2(@RequestParam MultiValueMap<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        // key - value가 한쌍이라도 ArrayList로 값을 저장해버리네
//        System.out.println(paramMap.get("age").getClass()); // class java.util.ArrayList
        return "ok";
    }

}
