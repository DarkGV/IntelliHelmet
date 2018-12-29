package pt.ubi.di.pmd.intellihelmet20;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class BluetoothConnection extends Service {

    TextView name, mac;
    Boolean result= false;
    BluetoothSocket socket;
    BluetoothDevice dev;
    DataInputStream dataInputStream;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public boolean createBond(BluetoothDevice btDevice)
                throws Exception {
            Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
            Method createBondMethod = class1.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
            return returnValue.booleanValue();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("DEVICE", device.getName());
                if (device.getName().equals("raspberrypi")) {
                    Log.d("DEVICE", "ENTERED");
                    try {
                        result = createBond(device);
                        if (result) {
                            Log.d("DEVICE", "WILL CONNECT");
                            socket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                            socket.connect();
                        }
                        Log.d("DEVICE", String.valueOf(result));
                        dev = device;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private final IBinder comm = new BluetoothComm();

    public class BluetoothComm extends Binder{
        BluetoothConnection getService(){
            return BluetoothConnection.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BLUETOOTHSERV", "I'm Running!!");
        BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> paired = myBluetooth.getBondedDevices();
        try {
            for (BluetoothDevice d : paired) {
                //Log.d("DEVICE", d.getName());
                if (d.getName().equals("raspberrypi") && d.getBondState() == 12) {
                    //Log.d("DEVICE", "ENTERED");
                    socket = d.createInsecureRfcommSocketToServiceRecord(d.getUuids()[0].getUuid());
                    socket.connect();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    //byte[] b = new byte[5];
                    //dataInputStream.read(b);
                    //Log.d("DEVICE", "Recebi " + String.valueOf(Float.parseFloat(new String(b))));
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return comm;
    }

    public String getDataFromBluetooth() throws IOException{
        byte[] b = new byte[5];
        dataInputStream.read(b);
        return new String(b);
    }

}

