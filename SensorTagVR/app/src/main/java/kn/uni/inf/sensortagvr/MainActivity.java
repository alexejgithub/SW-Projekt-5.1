package kn.uni.inf.sensortagvr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import kn.uni.inf.sensortagvr.gui_tab_management.TabSample;

public class MainActivity extends Activity {

    public static int firstActiveTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton recButt = (ImageButton) findViewById(R.id.recordButton);
        ImageButton vrButt = (ImageButton) findViewById(R.id.vrButton);
        ImageButton setButt = (ImageButton) findViewById(R.id.settingsButton);

        recButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstActiveTab = 0;
                Intent myIntent = new Intent(MainActivity.this,
                        TabSample.class);
                startActivity(myIntent);
            }
        });

        vrButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstActiveTab = 1;
                Intent myIntent = new Intent(MainActivity.this,
                        TabSample.class);
                startActivity(myIntent);
            }
        });

        setButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstActiveTab = 2;
                Intent myIntent = new Intent(MainActivity.this,
                        TabSample.class);
                startActivity(myIntent);
            }
        });

    }
}