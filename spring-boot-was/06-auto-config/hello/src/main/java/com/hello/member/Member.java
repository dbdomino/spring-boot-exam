package com.hello.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {
    private String memberId;
    private String name;
    public Member() {
    }
    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }
}
