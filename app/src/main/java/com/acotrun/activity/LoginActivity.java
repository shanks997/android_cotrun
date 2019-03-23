package com.acotrun.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.popWindow.LoginPopWindow;
import com.acotrun.utility.NetInfoUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_cancel;
    private EditText edt_id;
    private EditText edt_pwd;
    private Button btn_look;
    private Button btn_forgetPW;
    private Button btn_login;
    private Button btn_register;
    private LoginPopWindow login_pWin;

    private long exitTime = 0;
    private Handler hdr;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        iv_cancel = findViewById(R.id.iv_cancel);
        edt_id = findViewById(R.id.edt_userId);
        edt_pwd = findViewById(R.id.edt_passWord);
        btn_look = findViewById(R.id.btn_look);
        btn_forgetPW = findViewById(R.id.btn_forgetPW);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_look.setOnClickListener(this);
        btn_forgetPW.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        iv_cancel.bringToFront();
        clearButtonListener(edt_id, edt_pwd, iv_cancel);

        hdr = new Handler()
        {

            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:					///登陆成功
                        login_pWin.dismiss();
                        if(flag) {
                            LoginActivity.this.finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            edt_id.setText("");
                            edt_pwd.setText("");
                            // Alert 对话框放在 td 线程中会显示不出来
                            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                            alert.setTitle("Alert!");
                            alert.setMessage("账号密码有误，登陆失败！");
                            alert.setNegativeButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            // 显示
                            alert.show();
                        }
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_forgetPW) { // 点击了“忘记密码”按钮
            Intent intent = new Intent(this, ForgetPwdActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login) { // 点击了“登录”按钮
            if (edt_id.length() == 0) {
                Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (edt_pwd.length() == 0) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // LoginPopWindow在主线程的最后运行，不晓得为什么
                login_pWin = new LoginPopWindow(LoginActivity.this);
                login_pWin.showAtLocation(this.findViewById(R.id.login), Gravity.CENTER, 0, 0);
                new Thread() {
                // 匿名对象 形式为：new NoNameObject().fun();
                    @Override
                    public void run() {
                        try {
                            String sid = edt_id.getText().toString();
                            String spwd = edt_pwd.getText().toString();
                            flag = NetInfoUtil.isUser(sid, spwd);
                            Message msg=new Message();
                            msg.what=0;
                            hdr.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } else if (v.getId() == R.id.btn_register) { // 点击了“注册”按钮
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_look) { // 点击了“随便看看”按钮
            Intent intent = new Intent(this, MainActivity.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (login_pWin != null && login_pWin.isShowing()) {
                login_pWin.dismiss();
                return true;
            } else {
                exit();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.exit_again),
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}