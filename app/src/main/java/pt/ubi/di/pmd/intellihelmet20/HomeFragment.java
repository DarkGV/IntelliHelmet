package pt.ubi.di.pmd.intellihelmet20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
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
        warning=view.findViewById(R.id.noHelmetError);
        timeLabel = view.findViewById(R.id.wearTimeLabel);
        final Switch startService = view.findViewById(R.id.StartService);
        if(BluetoothConnection.isRunning())
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
                    communicationThread =new CommunicationThread();
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

        public void run(){
            while(!BluetoothConnection.isRunning());

            while(true){
                try {
                    final String[] temp = BluetoothConnection.getDataFromBluetooth();
                    commHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(Integer.valueOf(temp[0])== 0 && temp[2].equals("shock") ){
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
                                        alerta.setMessage("SHOCK, please don´t take of your helmet.\nPress OK to cancel this message!" + "00:"+ (millisUntilFinished/1000));
                                    }

                                    @Override
                                    public void onFinish() {
                                        //ENVIA MENSAGEM
                                            try {
                                                String phoneNo = null;
                                                DBHelper dbHelper = new DBHelper(getActivity());
                                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                                Cursor oInfo = db.query(dbHelper.M_TABLE_NAME, new String[] {dbHelper.M_COL3}, null, null, null, null, null);
                                                if(oInfo.moveToFirst())
                                                    phoneNo = String.valueOf(oInfo.getInt(0));
                                                if(phoneNo != null){
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(phoneNo, null, "Houve um acidente!!!!", null, null);
                                                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (Exception ex) {
                                                Toast.makeText(getActivity(), "HOUVE UM ERRO DURANTE O ENVIO",
                                                        Toast.LENGTH_LONG).show();
                                                ex.printStackTrace();
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
