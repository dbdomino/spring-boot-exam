package com.minod.core.service;

import com.minod.core.discount.DiscountPolicy;
import com.minod.core.member.Member;
import com.minod.core.order.Order;
import com.minod.core.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor // 의존성 주입시 필드 순서대로, 생성자 자동생성, ctrl+F12 로 확인가능
public class OrderServiceImpl2 implements OrderService {

    // final 쓰는 이유? 1. 생성자 주입을 쓸 때, setter 주입 쓰는걸 방지한다.
    // 2. 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.
    private final MemberRepository memberRepository ;
    private final DiscountPolicy discountPolicy;

/*    @Autowired // Autowired 시킨 생성자가 필요없다. @RequiredArgsConstructor 썻기때문, 롬복에서 지원, 쓰면 중복으로 떠서 컴파일애러나므로 주석침
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }*/

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
