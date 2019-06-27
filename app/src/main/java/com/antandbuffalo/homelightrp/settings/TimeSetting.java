package com.antandbuffalo.homelightrp.settings;

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
import android.widget.TextView;
import android.widget.Toast;

import com.antandbuffalo.homelightrp.R;
import com.antandbuffalo.homelightrp.handlers.SessionHandler;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.service.StorageService;
import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;

import java.sql.Time;

public class TimeSetting extends AppCompatActivity implements SessionHandler {
    TimeSettingViewModel timeSettingViewModel;
    TextView onTime, offTime;
    int onTimeHrs, offTimeHrs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timeSettingViewModel = ViewModelProviders.of(this).get(TimeSettingViewModel.class);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeSettingViewModel.initRetrofit(this);
        timeSettingViewModel.sessionHandler = this;

        onTime = findViewById(R.id.onTime);
        offTime = findViewById(R.id.offTime);

        // 360 / 24 = 15

        CircularSliderRange sliderRange = (CircularSliderRange) findViewById(R.id.circular);
        sliderRange.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override
            public void onStartSliderMoved(double pos) {
                Log.d("------------", "onStartSliderMoved:" + pos);
                onTimeHrs = (int)(pos / 15);
                onTime.setText(onTimeHrs + " HRS");
            }

            @Override
            public void onEndSliderMoved(double pos) {
                Log.d("------------", "onEndSliderMoved:" + pos);
                offTimeHrs = (int)(pos / 15);
                offTime.setText(offTimeHrs + " HRS");
            }

            @Override
            public void onStartSliderEvent(ThumbEvent event) {
                Log.d("============", "onStartSliderEvent:" + event);
            }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                Log.d("============", "onEndSliderEvent:" + event);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Mode mode = timeSettingViewModel.buildChangeModeTimeRequest(onTimeHrs, offTimeHrs);
            timeSettingViewModel.changeModeTime(mode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onModeChanged(Mode mode) {
        if(mode == null) {
            Log.d("MODE", "onModeChanged: Error occured");
            return;
        }
        Toast.makeText(TimeSetting.this, "Timing changed successfully", Toast.LENGTH_SHORT).show();
    }
}
