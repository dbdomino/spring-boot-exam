package com.typeconverter.converter;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode // 롬복의 @EqualsAndHashCode 를 넣으면 모든 필드를 사용해서 equals() , hashcode() 를 생성한다.
// 따라서 모든 필드의 값이 같다면 a.equals(b) 의 결과가 참이 된다.
public class IpPort {
    private String ip;
    private int port;
    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
