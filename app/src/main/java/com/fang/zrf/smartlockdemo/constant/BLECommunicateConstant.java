package com.fang.zrf.smartlockdemo.constant;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.fang.zrf.smartlockdemo.BluetoothLeService;

import java.util.UUID;

/**
 * Created by zrf on 2016/4/26.
 */
public class BLECommunicateConstant {
    //after connect success ，notify remotedevice
    public static String UUID_KEY_DATA ="0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR5 = "0000fff5-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR7 = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR8 = "0000fff8-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR9 = "0000fff9-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHARA = "0000fffa-0000-1000-8000-00805f9b34fb";
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    //every gattCharacteristic has limit ,its length depends on hw
    public static BluetoothGattCharacteristic gattCharacteristic_keydata = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char2 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char3 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char4 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char5 = null;

    //read the lock core id/set the lock core id (2016000001)
    public static BluetoothGattCharacteristic gattCharacteristic_char6 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char7 = null;

    //read the key id /set the key id(2016100001)
    public static BluetoothGattCharacteristic gattCharacteristic_char8 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char9 = null;

    //read the record / delete the key info (by key id)2016100002
    public static BluetoothGattCharacteristic gattCharacteristic_charA = null;

    public static final String SET_LOCK_ID_STR = "2016000001";
    public static final String SET_KEY_ID_STR = "2016100001";
    public static final String DELETE_KEY_ID_STR = "2016100002";


    public static final int DISCOVER_DEVICE = 0;
    public static final int WRITE_DATA = 1;
    public static final int READ_DATA = 2;
    public static final int CONNECTED = 3;
    public static final int DISCONNECTED = 4;
    public static final int READ_KEY_ID = 5;
    public static final int READ_LOCK_ID = 6;
    public static final int READ_RECORD = 7;

    public static final int SET_KEY_ID = 8;
    public static final int SET_LOCK_ID = 9;
    public static final int DELETE_KEY = 10;


}
