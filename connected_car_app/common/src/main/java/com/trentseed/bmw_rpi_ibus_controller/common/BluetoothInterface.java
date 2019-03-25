package com.trentseed.bmw_rpi_ibus_controller.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

/**
 * Handles Bluetooth communication with Raspberry Pi
 *
 */
public class BluetoothInterface {

    // bluetooth objects
    public static Activity mActivity;
    public static List<IBUSPacket> mArrayListIBUSActivity = new ArrayList<>();
    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothDevice mBluetoothDevice;
    public static BluetoothSocket mBluetoothSocket;
    public static InputStream mBluetoothInputStream;
    public static OutputStream mBluetoothOutputStream;
    public static UUID serviceUUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); // TODO move to config
    public static String remoteBluetoothAddress = "B8:27:EB:69:90:49";  // TODO move to config
    public static ConnectedThread listenThread;
    public static Boolean isConnecting = false;

    /**
     * Connects to Raspberry Pi via Bluetooth.
     * Note: Python services must be running on remote device.
     */
    public static boolean connectToRaspberryPi(){
        try{
            // connect to device and get input stream
            BluetoothInterface.isConnecting = true;
            mArrayListIBUSActivity = new ArrayList<>();
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(remoteBluetoothAddress);
            mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(serviceUUID);
            mBluetoothSocket.connect();
            mBluetoothInputStream = mBluetoothSocket.getInputStream();
            mBluetoothOutputStream = mBluetoothSocket.getOutputStream();
            BluetoothInterface.isConnecting = false;

            // start listening for data on new thread
            listenThread = new ConnectedThread();
            listenThread.start();
            return true;
        }catch(Exception e){
            BluetoothInterface.isConnecting = true;

//            if(mActivity != null && mActivity.isFinishing() == false){
//                Toast.makeText(mActivity, "Unable To Connect via Bluetooth", Toast.LENGTH_LONG).show();
//            }

            return false;
        }
    }

    /**
     * Determines if Bluetooth connection has been established with Raspberry Pi
     * @return boolean
     */
    public static boolean isConnected(){
        return mBluetoothAdapter != null && mBluetoothDevice != null && mBluetoothSocket.isConnected();
    }

    /**
     * Determines if Bluetooth is in progress with establishing connection
     * @return boolean
     */
    public static boolean isConnecting(){
        return isConnecting;
    }

    /**
     * Checks Bluetooth connection, and connects if necessary.
     */
    public static void checkConnection(){
        if(!BluetoothInterface.isConnected()){
            BluetoothInterface.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothInterface.connectToRaspberryPi();
        }
    }

    /**
     * Disconnect Bluetooth RFCOMM connection
     */
    public static void disconnect(){
        try{
            mBluetoothSocket.close();
        }catch(Exception e){
            Log.d("BMW", e.getMessage());
        }

        mBluetoothAdapter = null;
        mBluetoothDevice = null;
        mBluetoothSocket = null;
    }

    /**
     * Displays each paired bluetooth device info via Toast notification
     * @param activity
     */
    public static void viewPairedDevices(Activity activity){
        // show paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String text = device.getName() + "\n" + device.getAddress();
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
