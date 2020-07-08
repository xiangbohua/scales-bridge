package com.xiangbohua.open.protocol;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:07
 */
public enum  WeightType {
    /**
     * Weight type
     */
    GROSS(1, "GrossWeight"),
    NET(2, "NetWeight");
    private int value;
    private String name;

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return  this.name;
    }

    WeightType(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
