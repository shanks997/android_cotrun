package com.acotrun.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.tabFragment.HomeFragment;
import com.acotrun.tabFragment.MyselfFragment;

public class MainActivity extends Activity implements View.OnClickListener {

    private HomeFragment homeF;
    private MyselfFragment myselfF;
    private TextView homeT;
    private TextView myselfT;
    private FragmentManager fragM;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fragM = getFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_text:
                setTabSelection(0);
                break;
            case R.id.myself_text:
                setTabSelection(3);
                break;
        }
    }

    private void initViews() {
        homeT = findViewById(R.id.home_text);
        myselfT = findViewById(R.id.myself_text);
        homeT.setOnClickListener(this);
        myselfT.setOnClickListener(this);
    }

    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction fragTran = fragM.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragTran);
        switch (index) {
            case 0:
                // 当点击了 主页tab 时，改变控件的图片和文字颜色
                homeT.setTextColor(Color.parseColor("#008577"));
                if (homeF == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    homeF = new HomeFragment();
                    fragTran.add(R.id.content, homeF);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    fragTran.show(homeF);
                }
                break;
            case 3:
                // 当点击了 主页tab 时，改变控件的图片和文字颜色
                myselfT.setTextColor(Color.parseColor("#008577"));
                if (myselfF == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    myselfF = new MyselfFragment();
                    fragTran.add(R.id.content, myselfF);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    fragTran.show(myselfF);
                }
                break;
        }
        fragTran.commit();
    }

    private void clearSelection() {
        homeT.setTextColor(Color.parseColor("#82858b"));
        myselfT.setTextColor(Color.parseColor("#82858b"));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeF != null) {
            transaction.hide(homeF);
        }
        if (myselfF != null) {
            transaction.hide(myselfF);
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

