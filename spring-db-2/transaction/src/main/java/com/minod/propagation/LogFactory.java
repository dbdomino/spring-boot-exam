package com.minod.propagation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그 엔티티
 */
@Entity
@Getter @Setter
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR"
        , sequenceName = "log_factory_seq"
        , initialValue = 1
        , allocationSize = 1
)
public class LogFactory {
    @Id
//    @GeneratedValue  // 이거 없으면 시퀀스 만들어서 값을 할당해줘야 함.
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "USER_SEQ_GENERATOR"
    )
    private Long id;
    private String message;

    public LogFactory() {

    }
    public LogFactory(String message) {
        this.message = message;
    }


}
