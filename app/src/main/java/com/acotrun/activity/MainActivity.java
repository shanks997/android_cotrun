package com.acotrun.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.tabFragment.HomeFragment;
import com.acotrun.tabFragment.MyselfFragment;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btn0, btn1, btn2, btn3;
    private HomeFragment homeFragment;
    private MyselfFragment myselfFragment;
    private String account;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initViews();
        initFragment();
        // 当前登录的 用户名
        account = getIntent().getStringExtra("account");
    }

    private void initViews() {
        btn0 = findViewById(R.id.rb_home);
        btn3 = findViewById(R.id.rb_myself);
        radioGroup = findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                // 每次选中之前先清楚掉上次的选中状态
                clearSelection();
                // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
                hideFragments(ft);
                switch (checkedId) {
                    // 点击了 “首页”
                    case R.id.rb_home:
                        findViewById(R.id.head_main).setVisibility(View.GONE);
                        btn0.setTextColor(getResources().getColor(R.color.colorMain));
                        if (homeFragment == null) {
                            // 如果MessageFragment为空，则创建一个并添加到界面上
                            homeFragment = new HomeFragment();
                            ft.add(R.id.frame, homeFragment);
                        } else {
                            // 如果MessageFragment不为空，则直接将它显示出来
                            ft.show(homeFragment);
                        }
                        break;


                    case R.id.rb_myself:
                        findViewById(R.id.head_main).setVisibility(View.VISIBLE);
                        btn3.setTextColor(getResources().getColor(R.color.colorMain));
                        if (myselfFragment == null) {
                            // 如果MessageFragment为空，则创建一个并添加到界面上
                            myselfFragment = new MyselfFragment();
                            ft.add(R.id.frame, myselfFragment);
                        } else {
                            // 如果MessageFragment不为空，则直接将它显示出来
                            ft.show(myselfFragment);
                        }
                        break;
                }
                ft.commit();
            }

        });
    }

    private void initFragment() {
        FragmentManager ft = getSupportFragmentManager();
        FragmentTransaction fm = ft.beginTransaction();
        homeFragment = new HomeFragment();
        btn0.setTextColor(getResources().getColor(R.color.colorMain));
        findViewById(R.id.head_main).setVisibility(View.GONE);
        fm.add(R.id.frame, homeFragment);
        fm.commit();
    }

    private void clearSelection() {
        btn0.setTextColor(Color.parseColor("#82858b"));
        btn3.setTextColor(Color.parseColor("#82858b"));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (myselfFragment != null) {
            transaction.hide(myselfFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.exit_again),
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}

