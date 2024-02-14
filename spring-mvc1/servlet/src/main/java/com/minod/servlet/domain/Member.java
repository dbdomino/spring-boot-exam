package com.minod.servlet.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {
    private Long id;
    private String username;
    private int age;

    public Member() {

    }

    public Member(String usernane, int age) {
        this.username = usernane;
        this.age = age;

    }
}
