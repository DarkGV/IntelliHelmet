package pt.ubi.di.pmd.intellihelmet20;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class BluetoothConnection extends Service {

    private static boolean serviceRunning = false;
    private BluetoothSocket socket;
    private static DataInputStream dataInputStream = null;
    private static DataOutputStream dataOutputStream = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent oInt,int flags, int startId){
        BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> paired = myBluetooth.getBondedDevices();
        try {
            for (BluetoothDevice d : paired) {
                //Log.d("DEVICE", d.getName());
                if (d.getName().equals("raspberrypi") && d.getBondState() == 12) {
                    socket = d.createInsecureRfcommSocketToServiceRecord(d.getUuids()[0].getUuid());
                    socket.connect();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream= new DataOutputStream(socket.getOutputStream());
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        serviceRunning = true;

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            dataInputStream.close();
            socket.close();
        }catch(IOException e){
            System.out.println(e);
        }

        serviceRunning = false;
    }

    public static String[] getDataFromBluetooth() throws IOException{
        String[] values = new String[3];
        byte[] b = new byte[5];

        dataOutputStream.write('1');

        dataInputStream.read(b);
        values[0] = new String(b);

        dataInputStream.read(b);
        values[1] = new String(b);

        dataInputStream.read(b);
        values[2] = new String(b);

        Log.d("BLUETOOTHDATA", "Posto -> " + values[0] + "\nTemperatura -> " + values[1] +
                "\nChoque -> " + values[2]);

        return values;
    }


    public static boolean isRunning(){
        return serviceRunning;
    }




}

