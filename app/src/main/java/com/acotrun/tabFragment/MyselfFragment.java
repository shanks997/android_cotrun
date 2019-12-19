package com.acotrun.tabFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.acotrun.R;
import com.acotrun.activity.LoginActivity;
import com.acotrun.bean.ImageDownLoader;
import com.acotrun.bean.RoundImageView;


public class MyselfFragment extends Fragment implements View.OnClickListener  {

    ImageDownLoader imageDownLoader;
    RoundImageView imageView;
    String uidPic;
    Button btn_quit;
    Boolean is_login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myselflayout = inflater.inflate(R.layout.myself_layout, container, false);
        Bundle bundle = this.getArguments();
        is_login = bundle.getBoolean("is_login");
        btn_quit = myselflayout.findViewById(R.id.login_quit);
        btn_quit.setOnClickListener(this);
        if (is_login) {
            imageDownLoader = new ImageDownLoader();
            SharedPreferences sp = getActivity().getSharedPreferences("acotrun",
                    Context.MODE_PRIVATE);
            imageView = myselflayout.findViewById(R.id.imageView);
            uidPic = sp.getString("avatar", null);
            imageDownLoader.imgExcute(imageView, uidPic);
            imageView.setOnClickListener(this);
            imageDownLoader.cancelTask();
        } else {
            btn_quit.setText("点击登录");
        }
        return myselflayout;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_quit) {
            if (is_login) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Alert!");
                alert.setMessage("您确定要退出登录吗？");
                alert.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                            }
                        });
                alert.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.putExtra("is_login", false);
                                startActivity(intent);
                            }
                        });
                // 显示
                alert.show();
            } else {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("is_login", false);
                startActivity(intent);
            }
        }
        else if (v == imageView) { // 点击了头像
        }
    }

}