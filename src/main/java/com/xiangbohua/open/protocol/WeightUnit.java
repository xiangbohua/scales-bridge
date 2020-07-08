package com.xiangbohua.open.protocol;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:20
 */
public enum WeightUnit {

    /**
     * Weight unit
     */
    LB(1, "LB"),
    KG(2, "KG");

    private int code;
    private String name;

    WeightUnit(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
