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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.antandbuffalo.homelightrp.handlers.SessionHandler;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Session;

public class MainActivity extends AppCompatActivity implements SessionHandler {
    Session session;
    MainActivityViewModel mainActivityViewModel;
    int speed = 0;
    Switch onOff, nightModeSwitch;
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

        SeekBar speedBar = findViewById(R.id.speedBar);
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = i;
                Light lightReq = mainActivityViewModel.buildSpeedRequest(speed);
                mainActivityViewModel.changeLightSpeed(lightReq);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        onOff = findViewById(R.id.switchOnOff);
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    Light lightReq = mainActivityViewModel.buildLightRequest("on", speed);
                    mainActivityViewModel.changeLightStatus(lightReq);
                }
                else {
                    Light lightReq = mainActivityViewModel.buildLightRequest("off", speed);
                    mainActivityViewModel.changeLightStatus(lightReq);
                }
            }
        });

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

        Button btnInc = findViewById(R.id.increase);
        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speed < 5) {
                    Light lightReq = mainActivityViewModel.buildSpeedRequest(++speed);
                    mainActivityViewModel.changeLightSpeed(lightReq);
                }
            }
        });

        Button btnDec = findViewById(R.id.decrease);
        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speed > 0) {
                    Light lightReq = mainActivityViewModel.buildSpeedRequest(--speed);
                    mainActivityViewModel.changeLightSpeed(lightReq);
                }
            }
        });

        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Light lightReq = null;
                if(isChecked) {
                    onOff.setVisibility(View.INVISIBLE);
                    lightReq = mainActivityViewModel.buildModeRequest("night");
                }
                else {
                    onOff.setVisibility(View.VISIBLE);
                    lightReq = mainActivityViewModel.buildModeRequest("default");
                }
                mainActivityViewModel.changeMode(lightReq);
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
//        Button onButton = findViewById(R.id.btnOn);
//        Button offButton = findViewById(R.id.btnOff);
//        statusView.setBackgroundColor(getResources().getColor(R.color.onButtonPressed));
//        onButton.setEnabled(true);
//        offButton.setEnabled(true);
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
        setStatus(light);
    }

    @Override
    public void onGetLightStatus(Light light) {
        Log.d("lightStatus", light.getStatus());
        TextView statusView = findViewById(R.id.statusView);
        statusView.setBackgroundColor(getResources().getColor(R.color.onButtonPressed));

        speed = light.getSpeed() != null? light.getSpeed() : 0;

        onOff.setChecked(light.getStatus().equalsIgnoreCase("on"));

        nightModeSwitch.setChecked(light.getMode().equalsIgnoreCase("night"));

        setStatus(light);
    }

    public void setStatus(Light light) {
        TextView lastStatus = findViewById(R.id.lastStatus);
        if(light == null) {
            lastStatus.setText("Not able to connect");
            return;
        }

        lastStatus.setText("Status: " + light.getStatus() + " Interval: " + light.getInterval());

        TextView lblSpeed = findViewById(R.id.lblSpeed);
        lblSpeed.setText("Speed: " + speed);

        SeekBar speedIndicator = findViewById(R.id.speedBar);
        speedIndicator.setProgress(speed);
    }
}
