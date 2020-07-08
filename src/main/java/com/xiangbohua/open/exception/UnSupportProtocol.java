package com.xiangbohua.open.exception;

/**
 * @author xiangbohua
 * @date 2020/5/13 14:42
 */
public class UnSupportProtocol extends ScalesException {
    private String protocolName;
    public String getProtocolName() {
        return this.protocolName;
    }
    public UnSupportProtocol(String protocolName) {
        super("Protocol was not supported:" + protocolName);
        this.protocolName = protocolName;
    }
}
