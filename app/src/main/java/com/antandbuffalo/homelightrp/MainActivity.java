package com.antandbuffalo.homelightrp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.antandbuffalo.homelightrp.handlers.SessionHandler;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Session;
import com.antandbuffalo.homelightrp.service.StorageService;

public class MainActivity extends AppCompatActivity implements SessionHandler {
    Session session;
    MainActivityViewModel mainActivityViewModel;
    int speed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.initRetrofit();
        mainActivityViewModel.sessionHandler = this;
        mainActivityViewModel.getLightStatus();
        //mainActivityViewModel.createSession();

        Button btnSetIp = findViewById(R.id.setIp);
        btnSetIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ipAddress = findViewById(R.id.ipAddress);
                if(ipAddress != null && ipAddress.getText().toString() != null) {
                    mainActivityViewModel.setIpAddress(ipAddress.getText().toString());
                    mainActivityViewModel.initRetrofit();
                }
            }
        });


        Button btnOn = findViewById(R.id.btnOn);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Light light = mainActivityViewModel.buildLightRequest("on", speed);
                mainActivityViewModel.changeLightStatus(light);
            }
        });

        Button btnOff = findViewById(R.id.btnOff);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Light light = mainActivityViewModel.buildLightRequest("off");
                mainActivityViewModel.changeLightStatus(light);
            }
        });

        ProgressBar speedIndicator = findViewById(R.id.speedInd);

        Button btnInc = findViewById(R.id.increase);
        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speed < 5) {
                    Light lightReq = mainActivityViewModel.buildLightRequest("on", ++speed);
                    mainActivityViewModel.changeLightSpeed(lightReq);
                }
            }
        });

        Button btnDec = findViewById(R.id.decrease);
        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speed > 0) {
                    Light lightReq = mainActivityViewModel.buildLightRequest("on", --speed);
                    mainActivityViewModel.changeLightSpeed(lightReq);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sessionCreated(Session sess) {
        session = sess;
        System.out.println("Token is " + session.getToken());
        TextView statusView = findViewById(R.id.statusView);
        Button onButton = findViewById(R.id.btnOn);
        Button offButton = findViewById(R.id.btnOff);
        statusView.setBackgroundColor(getResources().getColor(R.color.onButtonPressed));
        onButton.setEnabled(true);
        offButton.setEnabled(true);
    }

    @Override
    public void messageCreated(Message message) {
        Log.d("messageCreated", message.getMessage());
        TextView lastStatus = findViewById(R.id.lastStatus);
        lastStatus.setText(message.getMessage());
    }

    @Override
    public void lightStatusChanged(Light light) {
        Log.d("lightStatusChanged", light.getStatus());
        TextView lastStatus = findViewById(R.id.lastStatus);
        lastStatus.setText("Status: " + light.getStatus() + " Interval: " + light.getInterval());

        TextView lblSpeed = findViewById(R.id.lblSpeed);
        lblSpeed.setText("Speed: " + speed);
    }

    @Override
    public void onGetLightStatus(Light light) {
        TextView lastStatus = findViewById(R.id.lastStatus);
        if(light == null) {
            lastStatus.setText("Not able to connect");
            return;
        }
        Log.d("lightStatus", light.getStatus());
        TextView statusView = findViewById(R.id.statusView);
        TextView lblSpeed = findViewById(R.id.lblSpeed);
        statusView.setBackgroundColor(getResources().getColor(R.color.onButtonPressed));
        lastStatus.setText("Status: " + light.getStatus() + " Interval: " + light.getInterval());
        speed = light.getSpeed() != null? light.getSpeed() : 0;
        lblSpeed.setText("Speed: " + speed);
    }
}
