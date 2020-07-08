package com.xiangbohua.open.protocol;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiangbohua
 * @date 2020/5/13 18:02
 */
public class WeightDataProtocolC implements WeightDataProtocol {

    private final static Map<String, WeightUnit> WEIGHT_UNIT_MAP = new HashMap<String, WeightUnit>(){
        {put("KG", WeightUnit.KG);}
        {put("LB", WeightUnit.LB);}
    };

    @Override
    public String protocolType() {
        return "C";
    }

    @Override
    public int totalDataLength() {
        return 8;
    }

    @Override
    public WeightType readWeightType(Byte[] data) {

        return null;
    }

    @Override
    public WeightUnit readWeightUnit(Byte[] data) {
        byte[] typeByte = new byte[]{data[14],data[15]};
        String weightType = new String(typeByte);
        if (WEIGHT_UNIT_MAP.containsKey(weightType.toUpperCase())) {
            return WEIGHT_UNIT_MAP.get(weightType.toUpperCase());
        }
        return null;
    }

    @Override
    public ScalesState readScalesState(Byte[] data) {

        return null;
    }

    @Override
    public BigDecimal readWeightValue(Byte[] data) {
        Byte[] weiValue = Arrays.copyOfRange(data, 6,13);
        byte[] byteValue = new byte[weiValue.length];
        for (int i = 0; i < weiValue.length; i++) {
            byteValue[i] = weiValue[i];
        }

        String decimalWeightValue = new String(byteValue).trim();
        return new BigDecimal(decimalWeightValue);
    }

    @Override
    public Byte getStartFlag() {
        return null;
    }

    @Override
    public Byte getStopFlag() {
        return '=';
    }

    @Override
    public WeightDTO readData(Byte[] data) {

        WeightType weightType = this.readWeightType(data);
        WeightUnit weightUnit = this.readWeightUnit(data);
        ScalesState scalesState = this.readScalesState(data);
        BigDecimal weightValue = this.readWeightValue(data);

        WeightDTO result = new WeightDTO();
        result.setWeight(weightValue);
        result.setWeightType(weightType);
        result.setWeightUnit(weightUnit);
        result.setResultState(scalesState);
        return result;
    }
}
