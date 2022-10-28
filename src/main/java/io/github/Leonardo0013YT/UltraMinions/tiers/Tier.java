package io.github.Leonardo0013YT.UltraMinions.tiers;

public class Tier {

    private int order;
    private int required;
    private int max;
    private String msg;

    public Tier(int order, int required, int max, String msg) {
        this.order = order;
        this.required = required;
        this.max = max;
        this.msg = msg;
    }

    public int getOrder() {
        return order;
    }

    public String getMsg() {
        return msg;
    }

    public int getRequired() {
        return required;
    }

    public int getMax() {
        return max;
    }
}