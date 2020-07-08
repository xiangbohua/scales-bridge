package com.xiangbohua.open.protocol;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:05
 */
public class WeightDTO {

    private BigDecimal weight;

    private WeightType weightType;

    private ScalesState resultState;

    private WeightUnit weightUnit;

    public BigDecimal getWeight() {
        return this.weight;
    }
    public WeightType getWeightType() {
        return this.weightType;
    }
    public ScalesState getResultState() {
        return this.resultState;
    }


    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setWeightType(WeightType weightType) {
        this.weightType = weightType;
    }

    public void setResultState(ScalesState resultState) {
        this.resultState = resultState;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String toReadableString() {
        StringBuilder rd = new StringBuilder();
        rd.append("State：");
        rd.append(this.getResultState() == null ? "UnKnown" : this.getResultState().getDesc());
        rd.append(" Type：");
        rd.append(this.getWeightType() == null ? "UnKnown" : this.getWeightType().getName());
        rd.append(" Unit：");
        rd.append(this.getWeightUnit() == null ? "UnKnown" : this.getWeightUnit().getName());
        rd.append(" Value：");
        rd.append(this.getWeight() == null ? "UnKnown" : this.getWeight());
        return rd.toString();
    }

    public boolean isSame(WeightDTO weightDto) {
        if (weightDto == null) {
            return false;
        }

        if (!Objects.equals(this.weight, weightDto.getWeight())) {
            return false;
        }

        if (!Objects.equals(this.weightType, weightDto.getWeightType())) {
            return false;
        }

        if (!Objects.equals(this.resultState, weightDto.getResultState())) {
            return false;
        }
        if (!Objects.equals(this.weightUnit, weightDto.getWeightUnit())) {
            return false;
        }
        return true;
    }

}
