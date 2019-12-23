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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout ll_kind;
    TextView tv_kind;
    TextView textView;
    AlertDialog.Builder builder;
    Button btn_ok;
    EditText edt_name;
    EditText edt_content;
    EditText edt_time;
    EditText edt_model;
    EditText edt_remind;
    String name;
    String content;
    String kind;
    String time;
    String model;
    String remind;
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
        ll_kind = findViewById(R.id.layout_kind);
        ll_kind.setOnClickListener(this);
        tv_kind = findViewById(R.id.tv);
        textView = findViewById(R.id.textView);
        btn_ok = findViewById(R.id.btn_finish);
        btn_ok.setOnClickListener(this);
        edt_name = findViewById(R.id.edt_actName);
        edt_name.setOnClickListener(this);
        edt_content = findViewById(R.id.edt_actBrief);
        edt_content.setOnClickListener(this);
        edt_time = findViewById(R.id.edt_actName2);
        edt_time.setOnClickListener(this);
        edt_model = findViewById(R.id.edt_actName3);
        edt_model.setOnClickListener(this);
        edt_remind = findViewById(R.id.edt_actName4);
        edt_remind.setOnClickListener(this);
        btn_delete = findViewById(R.id.btn_rm);
        btn_delete.setOnClickListener(this);

        Bundle bd = getIntent().getExtras();    //得到传过来的bundle
        if (bd != null && !bd.isEmpty()) {
            textView.setText("修改计划项");
            btn_delete.setVisibility(View.VISIBLE);
            edt_name.setText(bd.getString("name"));
            edt_content.setText(bd.getString("content"));
            tv_kind.setText(bd.getString("kind"));
            edt_time.setText(bd.getString("time"));
            edt_model.setText(bd.getString("model"));
            edt_remind.setText(bd.getString("remind"));
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:					// 注册成功
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
        if (v == ll_kind) {
            builder = new AlertDialog.Builder(this);
            builder.setItems(Constant.ACTIVITY_ITEMS, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    dialog.dismiss();
                    tv_kind.setText(Constant.ACTIVITY_ITEMS[index]);
                }
            });
            builder.show();
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
            kind = tv_kind.getText().toString().trim();
            time = edt_time.getText().toString().trim();
            model = edt_model.getText().toString().trim();
            remind = edt_remind.getText().toString().trim();
            if(name.equals("")) {
                Toast.makeText(this, "活动名称不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (content.equals("")) {
                Toast.makeText(this, "活动简述不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (time.equals("")) {
                Toast.makeText(this, "时间不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (model.equals("")) {
                Toast.makeText(this, "计划模式不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (remind.equals("")) {
                Toast.makeText(this, "提醒方式不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        User user = User.getInstance();
                        uid = user.getU_id();
                        flag = NetInfoUtil.addsche(name, content, kind, time, model, remind, uid);
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
