package com.hello.memory;

public class Memory {
    private long used; // 사용중인 메모리
    private long max;  // 사용가능한 최대 메모리

    public Memory() {
    }

    public Memory(long used, long max) {
        this.used = used;
        this.max = max;
    }

    public long getUsed() {
        return used;
    }

    public long getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Memory{" +
                "used=" + used +
                ", max=" + max +
                '}';
    }
}
