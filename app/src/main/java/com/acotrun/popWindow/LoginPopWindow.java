package com.acotrun.popWindow;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.acotrun.R;

public class LoginPopWindow extends PopupWindow {

    public LoginPopWindow(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.login_popwin, null);
        this.setContentView(v);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setFocusable(false);
        //实例化一个 ColorDrawable 颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置 SelectPicPopupWindow 弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

}
