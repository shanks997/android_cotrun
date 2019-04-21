package com.acotrun.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class ResponseUtil {

    // 写入用户名和密码及各项信息
    public static void writeUserInfo(Context context, List<String> list) {
        int k = 0;
        SharedPreferences sp = context.getSharedPreferences("acotrun",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", list.get(k++));
        editor.putString("password", list.get(k++));
        editor.putString("avatar", list.get(k++));
        editor.putString("attention", list.get(k++));
        editor.putString("fans", list.get(k++));
        editor.putString("menu", list.get(k++));
        editor.putString("random", list.get(k++));
        editor.putString("menuc", list.get(k++));
        editor.putString("randomc", list.get(k++));
        editor.commit();
    }

}
