package com.minod.itemservice.controller.login;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.member.Member;
import com.minod.itemservice.repository.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    /**
     * 테스트용 사용자 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemC", 5000, 5));
        itemRepository.save(new Item("itemD", 7000, 7));
        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test!");
        member.setName("테스터");
        memberRepository.save(member);
        member.setLoginId("t");
        member.setPassword("t");
        member.setName("t");
        memberRepository.save(member);
    }
}
