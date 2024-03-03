package com.minod.propagation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR"
        , sequenceName = "member_id_seq"
        , initialValue = 1
        , allocationSize = 1
)
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "USER_SEQ_GENERATOR"
    )
    private Long id;

    private String username;

    public Member() { // 기본 생성자는 JPA 스팩에서 요구하는거라 일딴 해줌.
    }
    public Member(String username) { // 생성자
        this.username = username;
    }
}
