package com.minod.core.discount;

import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;

        } else {
            return 0;
        }

    }
}
