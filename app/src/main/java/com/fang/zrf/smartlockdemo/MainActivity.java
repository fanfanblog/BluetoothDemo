package com.fang.zrf.smartlockdemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fang.zrf.smartlockdemo.constant.BLECommunicateConstant;
import com.fang.zrf.smartlockdemo.utils.BLECommunicateUtils;


import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {


    private Button mConnDevice;
    private Button mReadInfo;
    private Button mSettingInfo;
    private Button mConnInfo;

    private Button mBtn;

    private BluetoothAdapter mBtAdapter;

    private boolean mScanning = false;

    private static final long SCNNING_TIME = 10000;
    private static BluetoothLeService mBLEService = null;
    private BluetoothDevice mBluetoothDevice = null;
    private static final String BLE_DEVICE_NAME = "D4:F5:13:78:F1:7D";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = null;
            String str1 = null;
            if (msg.obj != null) {
                byte[] buffer = (byte[]) msg.obj;
                str1 = String.valueOf(msg.obj);
                str = new String(buffer, 0, buffer.length);
            }
            switch (msg.what) {
                case BLECommunicateConstant.DISCOVER_DEVICE:
                    BluetoothDevice device = msg.getData().getParcelable(BluetoothLeService.BLUETOOTH_DEVICE);
                    if (device != null) {
                        mConnInfo.setText(device.getAddress());
                        mConnInfo.setEnabled(true);
                        mBluetoothDevice = device;
                    }
                    break;
                case BLECommunicateConstant.WRITE_DATA:
                    //  2016/4/26 send message
                    mSettingInfo.setText("发送出去了数据");
                    break;
                case BLECommunicateConstant.READ_DATA:
                    //  2016/4/26 receive message
                    mReadInfo.setText(str1);
                    break;
                case BLECommunicateConstant.CONNECTED:
                    mConnDevice.setText("已成功连接到设备");
                    setViewsEnable(false, true, true, false);
                    break;
                case BLECommunicateConstant.DISCONNECTED:
                    mConnDevice.setText("未连接到任何设备");
                    setViewsEnable(true, false, false, true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // TODO: 2016/4/25 not support ble,finish?
            return;
        }

        final BluetoothManager mBtManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBtManager == null) {
            return;
        }
        mBtAdapter = mBtManager.getAdapter();
        if (mBtAdapter == null) {
            // TODO: 2016/4/25 if not support bt ,finish?
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
        initView();
        mBLEService = new BluetoothLeService(this);
        BLECommunicateUtils.setBLEService(mBLEService);
        mBLEService.setOnServiceDiscoverListener(mOnServiceDiscover);
        mBLEService.setOnDataAvailableListener(mOnDataAvailable);
        mBLEService.setOnDisconnectListener(mOnDisconnectListener);
        mBLEService.setOnConnectListener(mOnConnectListener);
    }

    /**
     * init views
     */
    private void initView() {
        mConnDevice = (Button) findViewById(R.id.connect_device);
        mReadInfo = (Button) findViewById(R.id.read_info);
        mSettingInfo = (Button) findViewById(R.id.setting_info);
        mConnInfo = (Button) findViewById(R.id.connect_info);
        mBtn = (Button) findViewById(R.id.btn);
        setViewsClickListener(mConnDevice, mReadInfo, mSettingInfo, mConnInfo, mBtn);
        setViewsEnable(true, false, false, true);
    }


    /**
     * add listener for views
     *
     * @param views views
     */
    private void setViewsClickListener(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    private void setViewsEnable(boolean conn, boolean read, boolean setting, boolean connInfo) {
        mConnDevice.setEnabled(conn);
        mReadInfo.setEnabled(read);
        mSettingInfo.setEnabled(setting);
        mConnInfo.setEnabled(connInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.connect_device:
                scanLeDevice(!mScanning);
                break;
            case R.id.read_info:
                BLECommunicateUtils.receiveData(BLECommunicateConstant.gattCharacteristic_char6);
                break;
            case R.id.setting_info:
                BLECommunicateUtils.sendData(BLECommunicateConstant.gattCharacteristic_charA, new byte[]{12, 43, 4, 3});
                break;
            case R.id.connect_info:
                mBLEService.connect(mBluetoothDevice);
                break;
            case R.id.btn:

                break;
            default:
                break;
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // TODO: 2016/4/25 startScaning
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    mBtAdapter.stopLeScan(mLeScanCallBack);
//                }
//            }, SCNNING_TIME);
            mScanning = true;
            mBtAdapter.startLeScan(mLeScanCallBack);
        } else {
            // TODO: 2016/4/25 stopScanning
            mScanning = false;
            mBtAdapter.stopLeScan(mLeScanCallBack);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device.getAddress().equals(BLE_DEVICE_NAME)) {
                if (mScanning) {
                    mBtAdapter.stopLeScan(mLeScanCallBack);
                    mScanning = false;
                }


                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BluetoothLeService.BLUETOOTH_DEVICE, device);
                msg.setData(bundle);
                msg.what = 0;
                mHandler.sendMessage(msg);

                mBLEService.connect(device);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
        mBLEService.disconnect();
        mBLEService.close();
    }

    /**
     * get the supported characteristics , maybe need to change
     *
     * @param gattServices gattServices
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            return;
        }
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                if (gattCharacteristic.getUuid().toString().equals(BLECommunicateConstant.UUID_KEY_DATA)) {
                    BLECommunicateConstant.gattCharacteristic_keydata = gattCharacteristic;
                    mBLEService.setCharacteristicNotification(gattCharacteristic, true);
                }
                if (gattCharacteristic.getUuid().toString().equals(BLECommunicateConstant.UUID_CHAR9)) {
                    BLECommunicateConstant.gattCharacteristic_char9 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(BLECommunicateConstant.UUID_CHAR6)) {
                    BLECommunicateConstant.gattCharacteristic_char6 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(BLECommunicateConstant.UUID_CHARA)) {
                    BLECommunicateConstant.gattCharacteristic_charA = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(BLECommunicateConstant.UUID_CHAR8)) {
                    BLECommunicateConstant.gattCharacteristic_char8 = gattCharacteristic;
                }
            }
        }
    }

    /**
     * find the service listener
     */
    private BluetoothLeService.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeService.OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLEService.getSupportedGattServices());
        }
    };

    /**
     * read and write listener
     */
    private BluetoothLeService.OnDataAvailableListener mOnDataAvailable = new BluetoothLeService.OnDataAvailableListener() {
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            mHandler.obtainMessage(BLECommunicateConstant.READ_DATA, characteristic.getValue()).sendToTarget();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            mHandler.obtainMessage(BLECommunicateConstant.WRITE_DATA, characteristic.getValue()).sendToTarget();
        }
    };

    /**
     * connect a device listener
     */
    private BluetoothLeService.OnConnectListener mOnConnectListener = new BluetoothLeService.OnConnectListener() {
        @Override
        public void onConnect(BluetoothGatt gatt) {
            mHandler.obtainMessage(BLECommunicateConstant.CONNECTED).sendToTarget();
        }
    };

    /**
     * disconnect a device listener
     */
    private BluetoothLeService.OnDisconnectListener mOnDisconnectListener = new BluetoothLeService.OnDisconnectListener() {
        @Override
        public void onDisconnect(BluetoothGatt gatt) {
            mHandler.obtainMessage(BLECommunicateConstant.DISCONNECTED).sendToTarget();
        }
    };
}

