package com.acotrun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.acotrun.bean.ActivityInfo;

import java.util.List;

public class PagerTabStripAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public PagerTabStripAdapter(FragmentManager fm) {
        super(fm);
    }

    public PagerTabStripAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    // 获取指定位置的碎片 Fragment
    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    // 获得指定碎片页的标题文本
    public CharSequence getPageTitle(int i) {
        return ActivityInfo.getName(i);
    }

}
