package com.antandbuffalo.homelightrp.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.antandbuffalo.homelightrp.R;
import com.antandbuffalo.homelightrp.handlers.ApiHandler;
import com.antandbuffalo.homelightrp.model.Duration;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Mode;
import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;

public class TimeSetting extends AppCompatActivity implements ApiHandler {
    TimeSettingViewModel timeSettingViewModel;
    TextView onTime, offTime;
    int onTimeHrs = 18, offTimeHrs = 22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CircularSliderRange sliderRange = (CircularSliderRange) findViewById(R.id.circular);

        timeSettingViewModel = ViewModelProviders.of(this).get(TimeSettingViewModel.class);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeSettingViewModel.initRetrofit(this);
        timeSettingViewModel.sessionHandler = this;

        onTime = findViewById(R.id.onTime);
        offTime = findViewById(R.id.offTime);

        sliderRange.setStartAngle(timeSettingViewModel.getStartAngle(this));
        sliderRange.setEndAngle(timeSettingViewModel.getStopAngle(this));

        onTime.setText(timeSettingViewModel.getLightStatus(this).getStartTime() + " Hrs");
        offTime.setText(timeSettingViewModel.getLightStatus(this).getStopTime() + " Hrs");

        // 360 / 24 = 15

        sliderRange.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override
            public void onStartSliderMoved(double pos) {
                Log.d("------------", "onStartSliderMoved:" + pos);
                onTimeHrs = (int)(pos / 15);
                onTime.setText(onTimeHrs + " Hrs");
            }

            @Override
            public void onEndSliderMoved(double pos) {
                Log.d("------------", "onEndSliderMoved:" + pos);
                offTimeHrs = (int)(pos / 15);
                offTime.setText(offTimeHrs + " Hrs");
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
            Duration duration = timeSettingViewModel.buildChangeDurationRequest(onTimeHrs, offTimeHrs);
            timeSettingViewModel.changeDuration(duration);
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
    public void onDurationChanged(Duration duration) {
        if(duration == null) {
            Log.d("Duration", "onDurationChanged: Error occured");
            return;
        }
        Light light = timeSettingViewModel.getLightStatus(this);
        light.setStartTime(onTimeHrs);
        light.setStopTime(offTimeHrs);
        timeSettingViewModel.updateLightStatus(this, light);
        Toast.makeText(TimeSetting.this, "Timing changed successfully", Toast.LENGTH_SHORT).show();
    }
}
