package com.minod.core.discount;

import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10%할인 적용")
    void vip_o() {
        //given
        Member member = new Member(1L, "memberA", Grade.VIP);

        //when
        int discount = discountPolicy.discount(member, 10000);
        //then
        assertThat(discount).isEqualTo(1000);
        // assertThat으로 검증할때는 alt+enter 눌러서 add on-demand static 으로 추가해서 쓰는게 유용하다.
    }

    @Test
    @DisplayName("VIP아니면 할인 적용 안됨")
    void vip_n() {
        //given
        Member member = new Member(2L, "memberA", Grade.BASIC);

        //when
        int discount = discountPolicy.discount(member, 10000);
        //then
//        assertThat(discount).isEqualTo(1000);
        assertThat(discount).isNotEqualTo(1000);
    }

}