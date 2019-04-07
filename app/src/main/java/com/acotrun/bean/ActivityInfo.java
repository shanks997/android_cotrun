package com.acotrun.bean;

import java.util.ArrayList;

public class ActivityInfo {

    public String name;

    public ActivityInfo() {
        name = "";
    }

    private static String[] mNameArray = {
            "饮食", "运动", "日常"
    };

    public static String getName(int i) {
        return mNameArray[i];
    }

    public static ArrayList<ActivityInfo> getList() {
        ArrayList<ActivityInfo> infoList = new ArrayList<ActivityInfo>();
        for (int i = 0; i < mNameArray.length; i++) {
            ActivityInfo info = new ActivityInfo();
            info.name = mNameArray[i];
            infoList.add(info);
        }
        return infoList;
    }

}
