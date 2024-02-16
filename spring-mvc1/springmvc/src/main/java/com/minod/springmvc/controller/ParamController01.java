package com.minod.springmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParamController01 {
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    // params의 조건이 반드시 있어야 허용, 아니면 400 Bad Request
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    // header에 조건이 반드시 있어야 허용, 아니면 404 not Found 뜸
    // header key 는 소문자여도 됨
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    // 헤더에 consumes 조건으로 Content-Type 조건이 맞아야 함. 아닐경우 405 Method Not Allowed 떳음.
    // consumes = "text/plain"
    //consumes = {"text/plain", "application/*"}
    //consumes = MediaType.TEXT_PLAIN_VALUE
    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     * produces = "text/plain"
     * produces = {"text/plain", "application/*"}
     * produces = MediaType.TEXT_PLAIN_VALUE
     * produces = "text/plain;charset=UTF-8"
     * response시 받는측에 적용되는 헤더를 지정하는 기능이다. produces
     * produces="application/text;charset=utf-8"  이런식으로 charset=utf-8 도 붙이면 한글깨짐도 막을 수 있다.
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html")
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
