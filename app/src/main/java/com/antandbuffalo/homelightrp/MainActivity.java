package com.antandbuffalo.homelightrp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.antandbuffalo.homelightrp.handlers.ApiHandler;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.model.Session;
import com.antandbuffalo.homelightrp.settings.Settings;

public class MainActivity extends AppCompatActivity implements ApiHandler {
    Session session;
    MainActivityViewModel mainActivityViewModel;
    int speed = 0;
    Switch onOff, nightModeSwitch;
    ProgressBar spinner;
    TextView statusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        statusView = findViewById(R.id.statusView);
        statusView.setVisibility(View.INVISIBLE);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.initIpAddress(getApplicationContext());
        mainActivityViewModel.initRetrofit();
        mainActivityViewModel.apiHandler = this;
        mainActivityViewModel.getLightStatus();

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
        nightModeSwitch.setText("Night Mode ("
                + mainActivityViewModel.getLightStatus(this).getStartTime()
                + " Hrs to "
                + mainActivityViewModel.getLightStatus(this).getStopTime()
                + " Hrs"
                +")");

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Mode modeReq = null;
                if(isChecked) {
                    onOff.setVisibility(View.INVISIBLE);
                    modeReq = mainActivityViewModel.buildModeRequest("night");
                }
                else {
                    onOff.setVisibility(View.VISIBLE);
                    modeReq = mainActivityViewModel.buildModeRequest("default");
                }
                mainActivityViewModel.changeMode(modeReq);
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
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void lightStatusChanged(Light light) {
        setStatus(light);
    }

    @Override
    public void onGetLightStatus(Light light) {
        spinner.setVisibility(View.INVISIBLE);
        statusView.setVisibility(View.VISIBLE);
        TextView lastStatus = findViewById(R.id.lastStatus);
        if(light == null) {
            lastStatus.setText("Current Action: Not able to connect");
            statusView.setBackgroundColor(getResources().getColor(R.color.offButtonPressed));
            return;
        }
        Log.d("lightStatus", light.getStatus());

        statusView.setBackgroundColor(getResources().getColor(R.color.onButtonPressed));

        speed = light.getSpeed() != null? light.getSpeed() : 0;

        onOff.setChecked(light.getStatus().equalsIgnoreCase("on"));

        nightModeSwitch.setChecked(light.getMode().equalsIgnoreCase("night"));

        setStatus(light);

        mainActivityViewModel.saveLightStatus(this, light);
    }

    @Override
    public void onModeChanged(Light light) {
        TextView lastStatus = findViewById(R.id.lastStatus);
        if(light == null) {
            lastStatus.setText("Current Action: Not able to change mode");
            return;
        }
        nightModeSwitch.setChecked(light.getMode().equalsIgnoreCase("night"));
        mainActivityViewModel.saveLightStatus(this, light);
        updateUI(light);
    }

    public void setStatus(Light light) {
        TextView lastStatus = findViewById(R.id.lastStatus);
        if(light == null) {
            lastStatus.setText("Current Action: Not able to connect");
            return;
        }

        lastStatus.setText("Current Action: " + light.getStatus());

        TextView lblSpeed = findViewById(R.id.lblSpeed);
        lblSpeed.setText("Speed: " + speed);

        SeekBar speedIndicator = findViewById(R.id.speedBar);
        speedIndicator.setProgress(speed);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("BACK", "need to refresh here");
        if(!mainActivityViewModel.isSameIp(this)) {
            mainActivityViewModel.initIpAddress(this);
            mainActivityViewModel.initRetrofit();
        }
        statusView.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);
        mainActivityViewModel.getLightStatus();
    }

    private void updateUI(Light light) {
        SeekBar speedIndicator = findViewById(R.id.speedBar);
        speedIndicator.setProgress(light.getSpeed());
        TextView lblSpeed = findViewById(R.id.lblSpeed);
        lblSpeed.setText("Speed: " + light.getSpeed());
        onOff.setChecked(light.getStatus().equalsIgnoreCase("on"));
        nightModeSwitch.setChecked(light.getMode().equalsIgnoreCase("night"));
    }
}

