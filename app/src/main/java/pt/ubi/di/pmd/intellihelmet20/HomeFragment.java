package pt.ubi.di.pmd.intellihelmet20;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;


public class HomeFragment extends Fragment {
    CommunicationThread communicationThread;
    private TextView txtTemp;
    private Handler commHandler = new Handler();
    private TextView warning, timeLabel;
    private long initialTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtTemp = view.findViewById(R.id.tempLabel);
        warning = view.findViewById(R.id.noHelmetError);
        timeLabel = view.findViewById(R.id.wearTimeLabel);
        final Switch startService = view.findViewById(R.id.StartService);
        if (BluetoothConnection.isRunning())
            startService.setChecked(true);

        else
            startService.setChecked(false);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothConnection.isRunning()) {
                    getActivity().stopService(new Intent(getActivity(), BluetoothConnection.class));
                    warning.setText("Please connect to the controler");
                    warning.setVisibility(View.VISIBLE);
                } else {
                    getActivity().startService(new Intent(getActivity(), BluetoothConnection.class));
                    communicationThread = new CommunicationThread();
                    communicationThread.start();
                    warning.setVisibility(View.INVISIBLE);

                }
            }
        });

        return view;
    }

    private class CommunicationThread extends Thread {

        private boolean okPressed = false;
        private AlertDialog alerta;
        private CountDownTimer timer;
        private boolean helmetOn = false;
        private String onMap = "http://www.google.com/maps/place/";


        public void run() {
            while (!BluetoothConnection.isRunning()) ;

            while (true) {
                try {
                    final String[] temp = BluetoothConnection.getDataFromBluetooth();
                    commHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Integer.valueOf(temp[0]) == 0 && temp[2].equals("shock")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setPositiveButton("I'm ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        okPressed = true;
                                        timer.cancel();
                                    }
                                });
                                alerta = builder.create();
                                alerta.setMessage("SHOCK, please don´t take of your helmet.\nPress OK to cancel this message!" + "00:10");
                                alerta.show();

                                timer = new CountDownTimer(10000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        alerta.setMessage("SHOCK, please don´t take of your helmet.\nPress OK to cancel this message!" + "00:" + (millisUntilFinished / 1000));
                                    }

                                    @Override
                                    public void onFinish() {
                                        //ENVIA MENSAGEM

                                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                                        LocationListener locationListener = new MyLocationListener();


                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            try {
                                                DBHelper dbHelper = new DBHelper(getActivity());
                                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                                Cursor oInfo = db.query(dbHelper.M_TABLE_NAME, new String[] {"*"}, null, null, null, null, null);
                                                if(oInfo.moveToFirst()){
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(String.valueOf(oInfo.getInt(oInfo.getColumnIndex(dbHelper.M_COL3))), null, "Houve um acidente!!\nNome: "+oInfo.getString(oInfo.getColumnIndex(dbHelper.M_COL1))+"\nTipo de Sangue: "+oInfo.getString(oInfo.getColumnIndex(dbHelper.M_COL2))+"\nNumero CC"+ String.valueOf(oInfo.getInt(oInfo.getColumnIndex(dbHelper.M_COL5))), null, null);
                                                    smsManager.sendTextMessage(String.valueOf(oInfo.getInt(oInfo.getColumnIndex(dbHelper.M_COL3))), null, "Houve um acidente!!\nNome: "+oInfo.getString(oInfo.getColumnIndex(dbHelper.M_COL1))+"\nTipo de Sangue: "+oInfo.getString(oInfo.getColumnIndex(dbHelper.M_COL2))+"\nNumero CC"+ String.valueOf(oInfo.getInt(oInfo.getColumnIndex(dbHelper.M_COL5))), null, null);
                                                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (Exception ex) {
                                                Toast.makeText(getActivity(), ex.toString(),
                                                        Toast.LENGTH_LONG).show();
                                                Log.d("SMSSENDER", String.valueOf(ex));
                                            }
                                            return;
                                        }

                                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 100, locationListener);
                                        Location l = null;
                                        for(String provider: locationManager.getAllProviders()) {
                                            l = locationManager.getLastKnownLocation(provider);
                                            if (l != null)
                                                break;
                                        }
                                            try {
                                                DBHelper dbHelper = new DBHelper(getActivity());
                                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                                Cursor oInfo = db.query(dbHelper.M_TABLE_NAME, new String[] {"*"}, null, null, null, null, null);
                                                if(oInfo.moveToFirst()){
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                                                    List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                                                    String fullAddress = addresses.get(0).getAddressLine(0);
                                                    smsManager.sendTextMessage(String.valueOf(oInfo.getInt(oInfo.getColumnIndex(dbHelper.M_COL3))), null, "Houve um acidente!!\nNome: "+oInfo.getString(oInfo.getColumnIndex(dbHelper.M_COL1))+"\nTipo de Sangue: "+oInfo.getString(oInfo.getColumnIndex(dbHelper.M_COL2))+"\nNumero CC: "+ String.valueOf(oInfo.getInt(oInfo.getColumnIndex(dbHelper.M_COL5))) + "\nLocalização: " + onMap + l.getLatitude() + "," + l.getLongitude()  , null, null);
                                                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (Exception ex) {
                                                Toast.makeText(getActivity(), ex.toString(),
                                                        Toast.LENGTH_LONG).show();
                                                Log.d("SMSSENDER", String.valueOf(ex));
                                            }
                                        alerta.dismiss();
                                    }
                                }.start();

                            }

                            else if(Integer.valueOf(temp[0]) == 0) {
                                txtTemp.setText(temp[1]);
                                warning.setVisibility(View.INVISIBLE);
                                if(!helmetOn)
                                    initialTime = System.currentTimeMillis();
                                helmetOn = true;
                                long currentTime = (System.currentTimeMillis() - initialTime);
                                timeLabel.setText(String.valueOf((currentTime/(1000 * 60)) % 60)+":"+String.valueOf((currentTime/1000) % 60));
                            }


                            else  {
                                warning.setText("Please put the helmet on");
                                warning.setVisibility(View.VISIBLE);
                                helmetOn = false;
                            }
                        }
                    });

                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }
}
