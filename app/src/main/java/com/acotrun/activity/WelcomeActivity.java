package com.acotrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.acotrun.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //创建Timer对象
        Timer timer = new Timer();
        TimerTask timerTask =new TimerTask() {
            @Override
            public void run() {
                // 这是在 TimerTask 类里面，故要写成 "WelcomeActivity.this" 而不是 "this"
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask, 2000);
    }
}
