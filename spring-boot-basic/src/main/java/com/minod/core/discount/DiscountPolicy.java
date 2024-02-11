package com.minod.core.discount;

import com.minod.core.member.Member;

public interface DiscountPolicy {

    int discount(Member member, int price);
}
