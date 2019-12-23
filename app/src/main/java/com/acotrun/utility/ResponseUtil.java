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
        editor.putString("uid", list.get(k++));
        editor.putString("pwd", list.get(k++));
        editor.putString("avatar", list.get(k++));
        editor.putString("sex", list.get(k++));
        editor.putString("model", list.get(k++));
        editor.commit();
    }

}
