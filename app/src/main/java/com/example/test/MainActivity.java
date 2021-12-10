package com.example.test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener,
        View.OnClickListener
{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_ENABLE_BT = 10;
    private FrameLayout frameMessage;
    private LinearLayout frameControls;

    private Switch switchEnableBt;
    private Button btnEnableSearch;
    private ProgressBar pbProgress;
    private ListView listBtDevices;

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameMessage = findViewById(R.id.frame_message);
        frameControls = findViewById(R.id.frame_control);

        switchEnableBt = findViewById(R.id.switch_enable_bt);
        btnEnableSearch = findViewById(R.id.btn_enable_search);
        pbProgress = findViewById(R.id.pb_progress);
        listBtDevices = findViewById(R.id.lv_bt_device);

        switchEnableBt.setOnCheckedChangeListener(this);
        btnEnableSearch.setOnClickListener(this);
        listBtDevices.setOnItemClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: " + getString(R.string.bt_not_supported));
            finish();
        }

        if (bluetoothAdapter.isEnabled()) {
            showFrameControls();
            switchEnableBt.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.equals(switchEnableBt)) {
            enableBt(isChecked);

            if (!isChecked) {
                showFrameMessage();
            }
        }
    }

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && bluetoothAdapter.isEnabled()) {
                        showFrameControls();
                    }
                    else if (result.getResultCode() == RESULT_CANCELED) {
                        enableBt(true);
                    }
                }
            });

    private void showFrameMessage() {
        frameMessage.setVisibility(View.VISIBLE);
        frameControls.setVisibility(View.GONE);
    }

    private void showFrameControls() {
        frameMessage.setVisibility(View.GONE);
        frameControls.setVisibility(View.VISIBLE);
    }

    private void enableBt(boolean flag) {
        if (flag) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mLauncher.launch(intent);
        }
        else {
            bluetoothAdapter.disable();
        }
    }

}