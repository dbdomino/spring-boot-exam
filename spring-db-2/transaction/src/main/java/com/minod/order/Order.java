package com.minod.order;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 // 테이블 없을 때 만들기 위해 spring.jpa.hibernate.ddl-auto 프로퍼티 옵션이 사용됨.
 // 시퀀스 설정, 기본increase는 50이다. https://velog.io/@gillog/JPA-%EA%B8%B0%EB%B3%B8-%ED%82%A4-%EC%83%9D%EC%84%B1-%EC%A0%84%EB%9E%B5IDENTITY-SEQUENCE-TABLE
 */
@Entity
@Getter // 예제를 단순하게 하기 위해 @Getter , @Setter 를 사용했다. 참고로 실무에서 엔티티에 @Setter 를 남발해서 불필요한 변경 포인트를 노출하는 것은 좋지 않다.
@Setter
@ToString
@Table(name="orders") // 테이블 명과 매핑 시켜주는 어노테이션, 왜씀?  테이블 이름을 지정하지 않으면 테이블 이름이 클래스 이름인 order 가 된다.
@SequenceGenerator(  // 시퀀스 기본 증가값이 50이라서 별도 설정해주려면 이거 필요하다.
        name = "orders_seq_generator",
        sequenceName = "orders_seq",
        initialValue=1, //시작값
        allocationSize=1
)
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,//사용할 전략을 시퀀스로 선택
            generator="orders_seq_generator"//식별자 생성기를 설정해놓은 orders_seq_generator 으로설정
            )
    private Long id;
    private String username;  //정상, 예외, 잔고부족
    private String payStatus; //대기, 완료

}
