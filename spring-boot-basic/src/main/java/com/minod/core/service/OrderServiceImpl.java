package com.minod.core.service;

import com.minod.core.annotation.MainDiscountPolicy;
import com.minod.core.discount.DiscountPolicy;
import com.minod.core.member.Member;
import com.minod.core.order.Order;
import com.minod.core.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  // 자동으로 스프링Bean으로 등록하는 방법, 의존성주입은 아래에 별도로 설정해줘야한다, 생성자주입, 필드주입
// @Component로 bean 등록할 경우 실글톤으로 등록한다.
public class OrderServiceImpl implements OrderService {

    // 아래 2개를 보면 DiscountPolicy를 선언해서 사용하고 있지만, new FixDiscountPolicy 처럼 고정된 구현클래스에도 의존하고 있다.
    // 추상에만 의존해야 DIP(의존관계 역전 원칙) 를 만족한다고 한다.
    // 여기서 new FixDiscountPolicy()를 new RateDiscountPolocy()로 바꾸게되면 소스코드 변경이 일어나므로
    // OCP(개방 폐쇄 원칙)을 위반한다고 한다.

//    private MemberRepository memberRepository = new MemoryMemberRepository();
//    private DiscountPolicy discountPolicy = new FixDiscountPolicy();

    // DIP를 만족시키게 하려면? 아래처럼 써야하는데, 그럼 nullpointException 이 날것이다.
    // 관심사를 분리하자... 이게뭔소리냐 인터페이스만 보고, 인터페이스에 new로 구현체를 지정하는 것은 다른것에 맡기자는 것이다.
    // 이걸 스프링에 맡기고 @autowired 시키는데, 이걸 관심사 분리 라고함.. 즉 new로 선택하는걸(구현객체를 선택하는걸) @autowired에 맡기는 것이다.
    // 이건 어디서 관리한다? Config로 Bean 선택해서 변경하고 거기서 관리한다.
//    private  DiscountPolicy discountPolicy;
//    private  MemberRepository memberRepository;
    // final 쓰는 이유? 1. 생성자 주입을 쓸 때, setter 주입 쓰는걸 방지한다.
    // 2. 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.
    private final MemberRepository memberRepository ;
    private final DiscountPolicy discountPolicy;
//    @Autowired private DiscountPolicy rateDiscountPolicy;// Autowired 필드명을 빈 이름으로 변경하면 두개의 빈이 충돌날때 이름으로 찾아준다고 하는데, 난 안됀다.

//    @Autowired private  DiscountPolicy discountPolicy; // 필드주입. 권장은안됨,  외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다.
//    @Autowired private  MemberRepository memberRepository; //  DI 프레임워크가 없으면 아무것도 할 수 없다


/*
//    @Autowired(required = false)  // setter주입(수정자 주입), 이런식으로 선택적으로 required 설정할 때  사용
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
//    @Autowired(required = false)
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*/

    //    @Autowired(required = false) // 생성자 주입이랑 중복되면 에러발생으로, 하나만 사용해야 한다,
    //@Autowired 의 기본 동작은 주입할 대상이 없으면 오류가 발생한다. 주입할 대상이 없어도 동작하게 하
    //려면 @Autowired(required = false) 로 지정하면 된다.
//    public OrderServiceImpl(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    // @Autowired 하나로 MemberRepository DiscountPolicy 같은 여러 의존관계를 한번에 주입시킬 수 있다.
    // Qualifier 는 쓰면 해깔 릴 수 있다고한다. 매칭시키는 기준용도 로만 사용하자.
    @Autowired // @Component로 클래스를 직접 bean으로 등록한다면, 의존관계 주입은 어떻게할지 정해야하는데, 생성자주입 방법으로 @Autowired걸어줘야한다. 이거말고도 필드주임, setter주입 있음.
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
//    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy")DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//        this.memberRepository = memberRepository;
//    }

//    @Autowired  // 메서드를 별개로 만들어서 주입함. 생성자주입 수정자주입에서 내부적으로 사용됨. 한번에 여러 필드를 주입 받을 수 있다. 잘사용되진않음
    // 어쩌면 당연한 이야기이지만 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작한다.
    //스프링 빈이 아닌 Member 같은 클래스에서 @Autowired 코드를 적용해도 아무 기능도 동작하지 않는다.
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        // ctrl alt v    // 단일책임 원칙을 잘 지켜서 설계하면
        int discountPrice= discountPolicy.discount(member, itemPrice);


        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
