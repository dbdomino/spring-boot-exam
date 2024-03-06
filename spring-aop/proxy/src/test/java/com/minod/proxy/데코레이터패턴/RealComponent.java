package com.minod.proxy.데코레이터패턴;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealComponent implements Component{
    @Override
    public String operation() {
        log.info("RealComponent 실행");
        return "data";// 걍 암거나씀.
    }
}
