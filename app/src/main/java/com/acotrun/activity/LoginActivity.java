package com.acotrun.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.utility.NetInfoUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_cancel;
    private EditText edt_id;
    private EditText edt_pwd;
    private Button btn_look;
    private Button btn_forgetPW;
    private Button btn_login;
    private Button btn_register;

    private TextView tv_test;

    Boolean flag;
    Context ct = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iv_cancel = findViewById(R.id.iv_cancel);
        edt_id = findViewById(R.id.edt_userId);
        edt_pwd = findViewById(R.id.edt_passWord);
        btn_look = findViewById(R.id.btn_look);
        btn_forgetPW = findViewById(R.id.btn_forgetPW);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        tv_test = findViewById(R.id.test);

        btn_look.setOnClickListener(this);
        btn_forgetPW.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        iv_cancel.bringToFront();
        clearButtonListener(edt_id, edt_pwd, iv_cancel);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_forgetPW) { // 点击了“忘记密码”按钮
            Intent intent = new Intent(this, ForgetPwdActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login) { // 点击了“登录”按钮
            if (edt_id.length() < 11) { // 手机号码不足11位
                Toast.makeText(ct, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            } else new Thread() {
            // 匿名对象 形式为：new NoNameObject().fun();
                @Override
                public void run() {
                    try {
                        String sid = edt_id.getText().toString();
                        String spwd = edt_pwd.getText().toString();
                        flag = NetInfoUtil.isUser(sid, spwd);
                        if (flag) {
                            tv_test.setText("flag1 = " + flag);
                        } else {
                            tv_test.setText("flag2 = " + flag);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }.start();
        } else if (v.getId() == R.id.btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public static void clearButtonListener(final EditText et_id, final EditText et_pwd, final View X) {
        // 取得 et 中的文字
        String etInputString = et_id.getText().toString();
        // 根据 et 中是否有文字进行 X 可见或不可见的判断
        if (TextUtils.isEmpty(etInputString)) {
            X.setVisibility(View.INVISIBLE);
        } else {
            X.setVisibility(View.VISIBLE);
        }
        //点击 X 时使 et 中的内容为空
        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_id.setText("");
                et_pwd.setText("");
                et_id.requestFocusFromTouch();
            }
        });
        //对 et 的输入状态进行监听
        et_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    X.setVisibility(View.INVISIBLE);
                } else {
                    X.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}