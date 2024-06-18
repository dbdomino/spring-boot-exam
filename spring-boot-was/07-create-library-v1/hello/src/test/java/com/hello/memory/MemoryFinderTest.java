package com.hello.memory;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemoryFinderTest {

    @Test
    void get() {
        // 간단한 테스트를 통해서 데이터가 조회 되는지 정도만 간단히 검증해보자.
        MemoryFinder memoryFinder = new MemoryFinder();
        Memory memory = memoryFinder.get();
        assertThat(memory).isNotNull();
    }

}