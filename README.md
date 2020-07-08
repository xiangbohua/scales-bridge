### 蓝牙电子称连接库
--

####功能
封装电子秤连接业务
支持连接使用蓝牙串口、USB串口的电子秤
通过添加协议即可支持不同的电子秤


#### 类型说明
WeightDataProtocol封装协议，此协议对字节流的结构和行为进行说明，包括
- 名称
- 数据长度
- 从字节数组中读取重量类型
- 从字节数组中读取重量单位
- 从字节数组中读取重量数值
- 定义字节数据的开始字符和终止字符

ScalesEventHandler事件委托，当成功读取到重量数值之后触发的事件，用于暴露给使用者

ProtocolFactory使用协议编号获取协议对象

#### 使用
``` 
    ScalesDataReader reader = new ScalesDataReader("协议编号"); //使用协议名称初始化库
    reader.setEventHandler(new Hander());//定义事件处理器
    scalesDataReader.startWork(socket.getInputStream());//已经连接的串口数据流
```
事件处理器定义
```
    /**
     * @author xiangbohua
     * @date 2020/5/14 14:14
     */
    private class Hander1 implements ScalesEventHandler {
        @Override
        public void onResult(WeightDTO weightDto) {
            System.out.println(weightDto.toReadableString());
        }
    }

```

--

#### 可选配置

setReadDelay(int):设置读取延时，设定读取数据的延时

setAcceptUnStableResult(bool):是否接受不稳定的电子称读数（注意，有些电子称可能没有不稳定结果，此时则默认为稳定）



#### Android系统中连接蓝牙电子称Demo

1.打开蓝牙
```
    if (!mBluetoothAdapter.isEnabled()) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
```
2.创建蓝牙监听代码
```
final BroadcastReceiver mReceiver = new BroadcastReceiver() {
   public void onReceive(Context context, Intent intent) {
       String action = intent.getAction();
       if (BluetoothDevice.ACTION_FOUND.equals(action)) {
           BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           Log.e("蓝牙设备已发现：", device.getName());
           if (device.getAddress().equals("98:D3:32:70:EB:67")) {
               connectThread = new ConnectThread(device);
               connectThread.start();
               context.unregisterReceiver(mReceiver);
           }
       }
   }
};
```
3.启动蓝牙监听服务
```
mBluetoothAdapter.startDiscovery();
```
4.开启线程连接蓝牙，并开始读取数据
```
    private class ConnectThread extends Thread {
    private final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private final BluetoothSocket socket;
    private final BluetoothDevice device;

    public ConnectThread(BluetoothDevice device) {
        this.device = device;
        BluetoothSocket tmp = null;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = tmp;
    }

    public void run() {
        try {
            socket.connect();
            scalesDataReader = new ScalesDataReader("B");
            scalesDataReader.setEventHandler(new Hander1());
            scalesDataReader.startWork(socket.getInputStream());

        } catch (IOException | UnSupportProtocol e) {
            try {
                socket.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            return;
        }
    }
}
```