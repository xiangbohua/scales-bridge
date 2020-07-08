package com.xiangbohua.open.protocol;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:14
 */
public enum ScalesState {
    /**
     * Scale state
     */
    OVER_LOAD(0, "OverLoad"),
    STABLE(1, "Stable"),
    UN_STABLE(2, "Unstable");
    private int state;
    private String desc;

    public int getState() {
        return this.state;
    }

    public String getDesc() {
        return  this.desc;
    }

    ScalesState(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
