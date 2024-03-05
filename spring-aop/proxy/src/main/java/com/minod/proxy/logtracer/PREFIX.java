package com.minod.proxy.logtracer;

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
