package com.minod.advanced.logtracer;

public enum PREFIX {
    START("-->"), COMPLETE("<--"), EX("<X-");

    PREFIX(String s) {
        this.prefix= s;
    }
    private final String prefix;
    public String getPrefix() {
        return prefix;
    }
}
