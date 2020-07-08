package com.xiangbohua.open.protocol;

import com.xiangbohua.open.exception.UnSupportProtocol;
import com.xiangbohua.open.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Serial port scales data processing class
 * @author xiangbohua
 * @date 2020/5/19 14:10
 */
public class ScalesDataReader {
    /**
     * Protocol name
     */
    private final String protocolName;
    /**
     * Protocol object
     */
    private final WeightDataProtocol protocol;
    /**
     * Event handler
     */
    private ScalesEventHandler eventHandler;

    /**
     * Data buffer
     */
    private final ByteBuffer byteBuffer;

    /**
     * Data input
     */
    private InputStream inputStream;

    /**
     * Whether or not accept the unstable result
     */
    private volatile boolean acceptUnStableResult = false;

    /**
     * Setting reading thread sleep time
     */
    private volatile int readDelay = 100;

    /**
     * The reading thread sleep time
     * Small value was suggested because the data will accumulation and the result will delay
     */
    private final int receiveDelay = 10;

    /**
     * The last result
     */
    private WeightDTO lastResult;

    /**
     * An switch of reading thread.
     * The reading thread will stop work if setting this switch to false.
     * Bug the receiving thread will keep working to consume the data
     */
    private volatile boolean isReadingData = false;

    private final int receiveLength;

    private final ReadThread readThread;
    private final ReceiveThread receiveThread;

    public ScalesDataReader(String protocolName) throws UnSupportProtocol {
        this.protocolName = protocolName;
        this.protocol = ProtocolFactory.getProtocolByName(protocolName);
        //The length of saving data was 3 times as the protocol data length.
        this.receiveLength = this.protocol.totalDataLength() * 3;
        this.byteBuffer = ByteBuffer.allocate(this.receiveLength);
        this.receiveThread = new ReceiveThread();
        this.readThread = new ReadThread();
    }

    /**
     * Return protocol name
     * @return
     */
    public String getProtocolName() {
        return this.protocolName;
    }

    /**
     * Return protocol object
     * @return
     */
    public WeightDataProtocol getProtocol() {
        return this.protocol;
    }

    /**
     * Setting the sleep time of read thread
     * Unit:mills second
     * @param delay
     */
    public void setReadDelay(int delay) {
        if (delay > 0) {
            this.readDelay = delay;
        }
    }

    /**
     * Set the handler that will process the weight result
     * Such as showing the numbers on a text input
     * @param eventHandler
     */
    public void setEventHandler(ScalesEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Provide an option: whether or not to exception an unstable result
     * Some protocol maybe report an unstable result
     * @param acceptUnStableResult
     */
    public void setAcceptUnStableResult(boolean acceptUnStableResult) {
        this.acceptUnStableResult = acceptUnStableResult;
    }

    /**
     * Start work
     * Start accept and read thread
     * @param inputStream Input stream after the scales was connected
     */
    public void startWork(InputStream inputStream) {
        this.inputStream = inputStream;
        this.receiveThread.start();
        this.readThread.start();
    }

    /**
     * Stop threads
     */
    public void stopWork() {
        this.readThread.interrupt();
        this.receiveThread.interrupt();
    }

    /**
     * Put the bytes array into an array
     * @param readBytes
     */
     private void putReceiveByte(byte[] readBytes) {
        synchronized (byteBuffer) {
            if (byteBuffer.remaining() < readBytes.length) {
                byteBuffer.flip();
            }
            byteBuffer.put(readBytes);
        }
    }

    /**
     * Read a group of data from the buffer
     */
    public void readData() {
        try {
            ScalesDataReader.this.isReadingData = true;
            byte[] streamFragment = byteBuffer.array();
            byte[] protocolBytes = this.extractProtocolBytes(streamFragment);
            WeightDTO result = ScalesDataReader.this.protocol.readData(ByteUtil.convert(protocolBytes));
            ScalesDataReader.this.onWeightRead(result);
            ScalesDataReader.this.isReadingData = false;
        } catch (Exception ex) {
            ScalesDataReader.this.isReadingData = false;
        }
    }

    /**
     * Process the weight data after read
     * @param result
     */
    private void onWeightRead(WeightDTO result) {
        System.out.println("Weight data received!" + result.toReadableString());
        if (!this.acceptUnStableResult && ScalesState.UN_STABLE.equals(result.getResultState())) {
            return;
        }

        //Skip if current result was the same as the last result
        if (null != this.lastResult && this.lastResult.isSame(result)) {
            return;
        }
        this.lastResult = result;
        if (null != this.eventHandler) {
            this.eventHandler.onResult(result);
        }
    }

    /**
     * Read a group of data from the buffer.
     * the length of the buffer was 3 times as the length of the protocol specified
     * @param streamFragment
     * @return
     */
    private byte[] extractProtocolBytes(byte[] streamFragment) {
        int firstStartIndex = -1;
        Byte startFlag = protocol.getStartFlag();


        for (int i = 0; i < streamFragment.length; i++) {
            byte currentByte = streamFragment[i];
            if (firstStartIndex == -1 && Objects.equals(currentByte, startFlag)) {
                firstStartIndex = i;
            }
        }

        byte[] result = new byte[this.protocol.totalDataLength()];
        int index = 0;
        int endIndex = this.protocol.totalDataLength() + firstStartIndex + 1;
        for (int j = firstStartIndex + 1; j < endIndex; j++) {
            result[index] = streamFragment[j];
            index ++;
        }
        return result;

    }

    /**
     * Define the data receiving thread
     */
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            byte[] readBytes = new byte[ScalesDataReader.this.receiveLength];
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    inputStream.read(readBytes);

                    if (!isReadingData) {
                        putReceiveByte(readBytes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(ScalesDataReader.this.receiveDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Define the data reading thread
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    ScalesDataReader.this.readData();
                    Thread.sleep(ScalesDataReader.this.readDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
