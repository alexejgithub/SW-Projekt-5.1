package kn.uni.inf.sensortagvr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import kn.uni.inf.sensortagvr.stor.StorageMainService;

public class RecordFragment extends Fragment implements View.OnClickListener{

    View view;
    StorageMainService storageService;
    boolean storageServiceBound = false;
    private ServiceConnection storageConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StorageMainService.StorageBinder binder = (StorageMainService.StorageBinder) service;
            storageService = binder.getService();
            storageServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            storageServiceBound = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view =  inflater.inflate(R.layout.fragment_record, container, false);

        final Button calibrateButton = (Button) view.findViewById(R.id.callibrate);
        calibrateButton.setOnClickListener(this);

        Intent bindStorService = new Intent(getActivity(), StorageMainService.class);
        getActivity().bindService(bindStorService, storageConnection, Context.BIND_AUTO_CREATE);


        final Button buttonMD = (Button) view.findViewById(R.id.measure);
        buttonMD.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (storageServiceBound)
                    storageService.measureData();
                else
                    Toast.makeText(getActivity().getApplicationContext(), "StorageService not connected", Toast.LENGTH_SHORT).show();
            }
        });

      /*  final Button writeButton = (Button) getView().findViewById(R.id.write);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storageService.closeMeasureSession();
                Toast.makeText(getActivity().getApplicationContext(), "Write", Toast.LENGTH_SHORT).show();
            }
        }); */

        final Button liveDataButt = (Button) view.findViewById(R.id.button_liveData);
        liveDataButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LiveDataFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

    public void onClick(View v) {
        if (storageServiceBound) {
        }
        //storageService.calibrate();
        else
            Toast.makeText(getActivity().getApplicationContext(), "StorageService not connected", Toast.LENGTH_SHORT).show();
    }


}
