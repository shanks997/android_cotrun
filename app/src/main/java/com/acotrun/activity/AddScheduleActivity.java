package com.acotrun.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.bean.User;
import com.acotrun.utility.BitmapUtils;
import com.acotrun.utility.Constant;
import com.acotrun.utility.NetInfoUtil;
import com.acotrun.utility.ResponseUtil;
import com.acotrun.utility.UploadUtil;

import java.io.File;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_return;
    Spinner spinner;
    TextView textView;
    AlertDialog.Builder builder;
    Button btn_ok;
    EditText edt_name;
    EditText edt_content;
    EditText edt_time;
    String kind;
    String name;
    String content;
    String time;
    String uid;
    boolean flag;
    Handler handler;
    Button btn_delete;
    Context ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ct = this;
        setContentView(R.layout.activity_add_schedule);
        iv_return = findViewById(R.id.btn_return);
        iv_return.setOnClickListener(this);
        textView = findViewById(R.id.textView);
        btn_ok = findViewById(R.id.btn_finish);
        btn_ok.setOnClickListener(this);
        edt_name = findViewById(R.id.edt_actName);
        edt_name.setOnClickListener(this);
        edt_time = findViewById(R.id.edt_actTime);
        edt_time.setOnClickListener(this);
        edt_content = findViewById(R.id.edt_actBrief);
        edt_content.setOnClickListener(this);
        btn_delete = findViewById(R.id.btn_rm);
        btn_delete.setOnClickListener(this);
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(pos == 1) {
                    edt_time.setVisibility(View.VISIBLE);
                } else edt_time.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Constant.ACTIVITY_ITEMS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

        Bundle bd = getIntent().getExtras();    //得到传过来的bundle
        if (bd != null && !bd.isEmpty()) {
            textView.setText("修改计划项");
            btn_delete.setVisibility(View.VISIBLE);
            edt_name.setText(bd.getString("name"));
            edt_content.setText(bd.getString("content"));
            edt_time.setText(bd.getString("time"));
            spinner.setSelection(Integer.parseInt(bd.getString("kind")));
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        if (flag) {
                            Toast.makeText(AddScheduleActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            AddScheduleActivity.this.finish();

                        } else {
                            Toast.makeText(AddScheduleActivity.this, "添加失败，该计划已存在", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if (flag) {
                            Toast.makeText(ct, "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ct, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == iv_return) {
            finish();
        }
        if (v == btn_delete) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ct);
            alert.setTitle("Alert!");
            alert.setMessage("您确定删除吗？");
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
                            new Thread() {
                                // 匿名对象 形式为：new NoNameObject().fun();
                                @Override
                                public void run() {
                                    try {
                                        User user = User.getInstance();
                                        name = edt_name.getText().toString().trim();
                                        uid = user.getU_id();
                                        flag = NetInfoUtil.deletesche(name, uid);
                                        Thread.sleep(500);
                                        finish();
                                        System.out.println("message-----------deletesche");
                                        System.out.println("message："+name+","+uid);
                                        Message msg=new Message();
                                        msg.what=1;
                                        handler.sendMessage(msg);
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    });
            // 显示
            alert.show();
        }
        if (v == btn_ok) {
            name = edt_name.getText().toString().trim();
            content =edt_content.getText().toString().trim();
            kind = String.valueOf(spinner.getSelectedItemPosition()).trim();
            time = edt_time.getText().toString().trim();
            if(name.equals("")) {
                Toast.makeText(this, "活动名称不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (content.equals("")) {
                Toast.makeText(this, "活动简述不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        User user = User.getInstance();
                        uid = user.getU_id();
                        flag = NetInfoUtil.addsche(kind, name, content, time, uid);
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

}
