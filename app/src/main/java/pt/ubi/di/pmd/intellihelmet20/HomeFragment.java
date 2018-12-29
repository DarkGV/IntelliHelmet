package pt.ubi.di.pmd.intellihelmet20;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private ServiceConnection comm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final Switch startService = view.findViewById(R.id.StartService);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Main.isServiceRunning()){
                    getActivity().unbindService(comm);
                }
                else{
                    Intent serviceToStart = new Intent(getActivity(), BluetoothConnection.class);
                    getActivity().bindService(serviceToStart, comm, getActivity().BIND_AUTO_CREATE);
                    return;
                }

            }
        });
        return view;
    }

    public void recvService(ServiceConnection comm) {
        this.comm = comm;
        return;
    }


}
