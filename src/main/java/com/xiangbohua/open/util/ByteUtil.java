package com.xiangbohua.open.util;

import java.io.UnsupportedEncodingException;

/**
 * @author xiangbohua
 * @date 2020/5/13 18:08
 */
public class ByteUtil {
    public static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] appendByte(byte[] a, byte b) {
        byte[] bArr = {b};
        return concat(a, bArr);
    }

    public static byte[] preAppend(byte[] source, byte[] pre) {
        byte[] c = new byte[source.length + pre.length];
        System.arraycopy(pre, 0, c, 0, pre.length);
        System.arraycopy(source, 0, c, source.length, source.length);
        return c;
    }

    public static byte[] convert(Byte[] bytePack) {
        byte[] res = new byte[bytePack.length];
        for(int i = 0; i < bytePack.length; i++) {
            res[i] = bytePack[i];
        }
        return res;
    }
    public static Byte[] convert(byte[] bytePack) {
        Byte[] res = new Byte[bytePack.length];
        for(int i = 0; i < bytePack.length; i++) {
            res[i] = bytePack[i];
        }
        return res;
    }

    public static String byteArrayToString(byte[] src) {
        try {
            return new String(src, "ASCII");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
