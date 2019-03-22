package com.acotrun.tabFragment;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.acotrun.R;
import com.acotrun.popWindow.LoginPopWindow;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btn;
    private TextView tv;

//    private Handler hdr;
//    private ViewPager vp; // /广告栏
//
//    final int CLOCK = 0; // 定时器

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn = getActivity().findViewById(R.id.btn_0);
        tv = getActivity().findViewById(R.id.home_text);
        btn.setOnClickListener(this);

//        hdr = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case CLOCK: // 按时走广告
//                        vp.setCurrentItem(vp.getCurrentItem() + 1);
//                        break;
//                }
//            }
//        };
//        new Thread() {
//            public void run() {
//                while (true) {
//                    try {
//                        sleep(2000);
//                        hdr.sendEmptyMessage(CLOCK);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homelayout = inflater.inflate(R.layout.home_layout, container, false);
        return homelayout;
    }

    @Override
    public void onClick(View v) {
        if (v == btn) tv.setText("Button clicked");
    }

}