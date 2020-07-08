package com.xiangbohua.open.protocol;

import java.math.BigDecimal;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:13
 */
public interface WeightDataProtocol {

    /**
     * Protocol type
     * @return
     */
    String protocolType();

    /**
     * Data length of this protocol
     * @return
     */
    int totalDataLength();

    /**
     * read the weight type
     * @param data
     * @return
     */
    WeightType readWeightType(Byte[] data);

    /**
     * read the weight unit
     * @param data
     * @return
     */
    WeightUnit readWeightUnit(Byte[] data);

    /**
     * read the scales state
     * @param data
     * @return
     */
    ScalesState readScalesState(Byte[] data);

    /**
     * read the weight value
     * @param data
     * @return
     */
    BigDecimal readWeightValue(Byte[] data);

    /**
     * define the start flag of this protocol
     * @return
     */
    Byte getStartFlag();

    /**
     * define the start flag of this protocol
     * @return
     */
    Byte getStopFlag();

    /**
     * 读取完整的重量数据
     * read entire weight data
     * @param data
     * @return
     */
    WeightDTO readData(Byte[] data);

}
