package com.acotrun.tabFragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.acotrun.R;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btn;
    private TextView tv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn = getActivity().findViewById(R.id.btn_0);
        tv = getActivity().findViewById(R.id.home_text);
        btn.setOnClickListener(this);

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