package com.jilou.ui.logic;

public enum RenderPriority {

    LOWEST(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    HIGHEST(4);

    private final int rate;

    RenderPriority(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

}
