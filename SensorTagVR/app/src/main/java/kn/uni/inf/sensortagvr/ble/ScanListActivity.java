package kn.uni.inf.sensortagvr.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import kn.uni.inf.sensortagvr.R;


/**
 *
 */
public class ScanListActivity extends AppCompatActivity {
    private static final long SCAN_PERIOD = 5000;
    private BluetoothLeScanner mLEScanner;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler myHandler;
    private Button scanButton;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private final ScanCallback mScanCallback = new ScanCallback() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            runOnUiThread(new Runnable() {
                /**
                 * {@inheritDoc}
                 *
                 * adds a device to the recycler view if it was found
                 */
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(result.getDevice());
                }
            });
        }

        /**
         *
         * {@inheritDoc}
         */
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("onBatchScanResult", sr.toString());
            }
        }

        /**
         *
         * {@inheritDoc}
         */
        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets up UI references.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setContentView(R.layout.activity_scanlist);
        RecyclerView mDeviceList = (RecyclerView) findViewById(R.id.scanlist);
        mDeviceList.setAdapter(mLeDeviceListAdapter);
        mDeviceList.setLayoutManager(new LinearLayoutManager(this));
        scanButton = (Button) findViewById(R.id.scan_toggle);

        myHandler = new Handler();


        // Initializes a Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 21 && mBluetoothAdapter != null) {
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            ScanFilter fi;

            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                    .build();

            fi = new ScanFilter.Builder()
                    .setDeviceName("CC2650 SensorTag")
                    .build();

            filters = new ArrayList<>();
            filters.add(fi);
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(true);
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        if (mBluetoothAdapter != null) {
            scanLeDevice(false);
            mLeDeviceListAdapter.clear();
        }

        super.onPause();
    }

    /**
     * @param enable if true the scan will start and stop after 5 seconds
     *               if false the scan will stop immediately
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            myHandler.postDelayed(new Runnable() {
                /**
                 * {@inheritDoc}
                 */
                @Override
                public void run() {
                    mLEScanner.stopScan(mScanCallback);

                }
            }, SCAN_PERIOD);
            mLEScanner.startScan(filters, settings, mScanCallback);
        } else {
            mLEScanner.stopScan(mScanCallback);
        }
    }


}


