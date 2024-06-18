package com.hello.domain;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MelloServe {
    private String Mello ;

    @PostConstruct
    public void init() {
        log.info("MelloServe.init gkg ");
    }

    public String getMello() {
        log.info("getMellow() {}",Mello);
        return Mello;
    }

    public void setMello(String mello) {
        Mello = mello;
    }
}
