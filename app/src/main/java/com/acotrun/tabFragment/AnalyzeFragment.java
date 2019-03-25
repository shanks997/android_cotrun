package com.acotrun.tabFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acotrun.R;

public class AnalyzeFragment extends Fragment implements View.OnClickListener {

    private View v_analyze;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v_analyze = inflater.inflate(R.layout.analyze_layout, container, false);
        return v_analyze;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

}
