package com.acotrun.tabFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acotrun.R;
import com.acotrun.bean.ImageDownLoader;
import com.acotrun.bean.RoundImageView;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageDownLoader imageDownLoader;
    RoundImageView imageView;
    String uidPic;
    Boolean is_login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homelayout = inflater.inflate(R.layout.home_layout, container, false);
        Bundle bundle = this.getArguments();
        is_login = bundle.getBoolean("is_login");
        if (is_login) {
            imageDownLoader = new ImageDownLoader();
            SharedPreferences sp = getActivity().getSharedPreferences("acotrun",
                    Context.MODE_PRIVATE);
            imageView = homelayout.findViewById(R.id.imgAvatar);
            uidPic = sp.getString("avatar", null);
            imageDownLoader.imgExcute(imageView, uidPic);
            imageView.setOnClickListener(this);
            imageDownLoader.cancelTask();
        }
        return homelayout;
    }

    @Override
    public void onClick(View v) {

    }

}