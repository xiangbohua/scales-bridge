package com.xiangbohua.open.protocol;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiangbohua
 * @date 2020/5/13 18:02
 */
public class WeightDataProtocolA implements WeightDataProtocol {

    private final static Map<String, ScalesState> STATE_MAP = new HashMap<String, ScalesState>(){
        {put("OL", ScalesState.OVER_LOAD);}
        {put("ST", ScalesState.STABLE);}
        {put("US", ScalesState.UN_STABLE);}
    };

    private final static Map<String, WeightType> WEIGHT_TYPE_MAP = new HashMap<String, WeightType>(){
        {put("GS", WeightType.GROSS);}
        {put("NT", WeightType.NET);}
    };

    private final static Map<String, WeightUnit> WEIGHT_UNIT_MAP = new HashMap<String, WeightUnit>(){
        {put("KG", WeightUnit.KG);}
        {put("LB", WeightUnit.LB);}
    };

    @Override
    public String protocolType() {
        return "A";
    }

    @Override
    public int totalDataLength() {
        return 18;
    }

    @Override
    public WeightType readWeightType(Byte[] data) {
        byte[] typeByte = new byte[]{data[3],data[4]};
        String weightType = new String(typeByte);
        if (WEIGHT_TYPE_MAP.containsKey(weightType)) {
            return WEIGHT_TYPE_MAP.get(weightType);
        }
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
        byte[] typeByte = new byte[]{data[0],data[1]};
        String state = new String(typeByte);
        if (STATE_MAP.containsKey(state)) {
            return STATE_MAP.get(state);
        }
        return null;
    }

    @Override
    public BigDecimal readWeightValue(Byte[] data) {
        //本协议重量数据位为：6-13
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
        return 0x0a;
    }

    @Override
    public Byte getStopFlag() {
        return 0x0d;
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
