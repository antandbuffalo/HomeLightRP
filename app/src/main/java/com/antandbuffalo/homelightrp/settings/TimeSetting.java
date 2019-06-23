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
import android.widget.Toast;

import com.antandbuffalo.homelightrp.R;
import com.antandbuffalo.homelightrp.handlers.SessionHandler;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.service.StorageService;

public class TimeSetting extends AppCompatActivity implements SessionHandler {
    TimeSettingViewModel timeSettingViewModel;
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
            Mode mode = timeSettingViewModel.buildChangeModeTimeRequest(18, 22);
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
