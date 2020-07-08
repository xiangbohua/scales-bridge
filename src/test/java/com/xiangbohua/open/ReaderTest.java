package com.xiangbohua.open;

import com.xiangbohua.open.protocol.ScalesDataReader;
import com.xiangbohua.open.util.ByteUtil;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author xiangbohua
 * @date 2020/5/14 14:12
 */
public class ReaderTest {

    @Test
    public void TestReader() {
        String s = "ST,GS,0018.000kg";
        byte[] test = s.getBytes();
        byte[] test2 = new byte[test.length + 2];
        int i = 0;
        for (; i < test.length; i++) {
            test2[i] = test[i];
        }
        test2[i] = 0x0d;
        i++;
        test2[i] = 0x0a;


        try {
            ScalesDataReader reader = new ScalesDataReader("B");
//            reader.startReceive();
//            reader.setEventHandler(new Hander1());
//            reader.putReceiveByte(test);

            //reader.putReceiveByte(test2);
            reader.readData();

            //reader.putReceiveByte(test2);
            byte[] tests = ByteUtil.appendByte(",GS,0018.000kg".getBytes(), (byte)0x0d);
            tests = ByteUtil.appendByte(tests, (byte)0x0a);

            //reader.putReceiveByte(tests);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
