package com.minod.core.discount;

import com.minod.core.annotation.MainDiscountPolicy;
import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("mainDiscountPolicy") // DiscountPolicy 주입할 때 중복이 되는 경우 구분자로 넣어 주는 것., 내부 문자는 컴파일에러로 잡아내지못하기에 어노테이션 구현해서 쓰는방법도 있음.
@MainDiscountPolicy // @Qualifier를 직접 어노테이션 만들어서 쓰기
@Primary
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;

        } else {
            return 0;
        }

    }
}
