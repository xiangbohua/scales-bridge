package com.xiangbohua.open.protocol;


import com.xiangbohua.open.exception.UnSupportProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:45
 */
public class ProtocolFactory {

    private static Map<String, WeightDataProtocol> protocols;
    static {
        protocols = new HashMap<>(3);
        protocols.put("B", new WeightDataProtocolB());
    }
    public static WeightDataProtocol getProtocolByName(String name) throws UnSupportProtocol {
        if (protocols.containsKey(name)) {
            return protocols.get(name);
        }
        throw new UnSupportProtocol(name);
    }


}
