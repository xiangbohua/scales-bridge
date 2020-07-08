package com.xiangbohua.open;

import com.xiangbohua.open.protocol.ScalesEventHandler;
import com.xiangbohua.open.protocol.WeightDTO;

/**
 * @author xiangbohua
 * @date 2020/5/14 14:14
 */
public class Hander1 implements ScalesEventHandler {
    @Override
    public void onResult(WeightDTO weightDto) {
        System.out.println(weightDto.toString());
    }
}
