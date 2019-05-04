package com.acotrun.tabFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acotrun.R;
import com.acotrun.adapter.RecordPageAdapter;
import com.acotrun.homeFragment.DietFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private View v_record;
    private PagerTabStrip pts_tab;
    private ViewPager vp;
    private List<Fragment> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v_record = inflater.inflate(R.layout.record_layout, container, false);
        return v_record;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPagerStrip();
        initViewPager();
    }

    // 初始化翻页标题栏
    private void initPagerStrip() {
        pts_tab = v_record.findViewById(R.id.pts_tab);
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        pts_tab.setTextColor(Color.BLACK);
    }

    // 初始化翻页视图
    private void initViewPager() {
        list=new ArrayList<>();
        list.add(new DietFragment());
        list.add(new DietFragment());
        list.add(new DietFragment());
        RecordPageAdapter adapter = new RecordPageAdapter(
                getFragmentManager(), list);
        // 从布局视图中获取名叫vp_content的翻页视图
        vp = v_record.findViewById(R.id.vp_content);
        vp.setAdapter(adapter);
        // 设置vp_content默认显示第一个页面
        vp.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        // TO DO..
    }

}