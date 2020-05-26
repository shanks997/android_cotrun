package com.acotrun.tabFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.utility.DatabaseOperation;
import com.acotrun.utility.NetInfoUtil;

public class CommunityFragment extends Fragment implements View.OnClickListener {

    private View v_analyze;
    Button btn_demo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v_analyze = inflater.inflate(R.layout.community_layout, container, false);
        btn_demo = v_analyze.findViewById(R.id.btn_demo);
        btn_demo.setOnClickListener(this);

        return v_analyze;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_demo) {
            DatabaseOperation.insert_db("name",
                    "creator", "_class", "content", "time");
        }
    }

}
