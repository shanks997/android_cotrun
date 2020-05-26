package com.acotrun.tabFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acotrun.R;
import java.util.List;

public class ActivityFragment extends Fragment implements View.OnClickListener {

    private View v_activity;
    private PagerTabStrip pts_tab;
    private ViewPager vp;
    private List<Fragment> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v_activity = inflater.inflate(R.layout.activity_layout, container, false);
        return v_activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        // TO DO..
    }

}