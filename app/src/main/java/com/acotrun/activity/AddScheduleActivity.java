package com.acotrun.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.acotrun.bean.Schedule;
import com.acotrun.bean.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    User user = new User();
    Handler handler = null;

    Button btnFinish=null;
    private TextView edtName;
    private TextView edtBrief;
    private TextView txtTime;
    private TextView txtPlan;
    private TextView txtRemind;
    private int hourOfDay,minute;

    public  static Schedule schedule=new Schedule();
    public static String addSchedule = null;

    public static List<Schedule> addsclist = new ArrayList<Schedule>();

    public int hourint=0;
    public  int minuint =0;

    Calendar calendar=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        //获取闹钟管理者
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        edtName=(TextView)findViewById(R.id.edt_actName);
        edtBrief=(TextView)findViewById(R.id.edt_actBrief);
        txtTime=(TextView) findViewById(R.id.txt_time);
        txtPlan=(TextView) findViewById(R.id.txt_planMode);
        txtRemind=(TextView) findViewById(R.id.txt_remindMode);

        Calendar calendar=Calendar.getInstance();
        hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        String hour=String.format("%02d",hourOfDay);
        hourint=hourOfDay;
        String min=String.format("%02d",minute);
        minuint=minute;
        txtTime.setText( hour + ":" + min);

        //时间
        findViewById(R.id.layout_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddScheduleActivity.this,new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        //btnTime.setPadding(10,10,20,10);
                        String hour=String.format("%02d",hourOfDay);
                        hourint=hourOfDay;
                        String min=String.format("%02d",minute);
                        minuint=minute;
                        txtTime.setText( hour + ":" + min);

                    }
                },hourOfDay,minute,true);
                timePickerDialog.show();
            }
        });

        //计划模式
        findViewById(R.id.layout_planMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(AddScheduleActivity.this);
                builder.setTitle("计划模式");
                final String[]  plan={"闹钟","推荐"};//0,1
                builder.setSingleChoiceItems(plan,2,new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        txtPlan.setText(plan[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        //提醒方式
        findViewById(R.id.layout_remindMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AddScheduleActivity.this);
                builder.setTitle("提醒方式");
                final String[]  remind={"响铃","振动"}; //0,1
                builder.setSingleChoiceItems(remind, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtRemind.setText(remind[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        user = LoginActivity.user;

        //完成
        btnFinish=(Button)findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getU_id()!=null) {

                    //简介
                    schedule.setSc_state(edtBrief.getText().toString());
                    //时间
                    String str=txtTime.getText().toString();
                    schedule.setSc_time(str.replace(":",""));
                    //用户id
                    schedule.setU_id(user.getU_id());
                    //操作符
                    schedule.setOperation("add");
                    //提醒方式
                    if(txtRemind.getText().toString().equals("响铃")) {
                        schedule.setSc_rem(0);
                    }
                    else {
                        schedule.setSc_rem(1); //振动
                    }
                    //用户模式
                    if(txtPlan.getText().toString().equals("闹钟")) {
                        schedule.setSc_mode(0);
                    }
                    else {
                        schedule.setSc_mode(1); //推荐
                    }

                    //标题
                    if(edtName.getText().toString()!=null){
                        schedule.setSc_name(edtName.getText().toString());
                    }else {
                        Toast.makeText(AddScheduleActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
                    }

                    init();
                    AddScheduleThread addScT = new AddScheduleThread(handler,schedule);
                    addScT.start();


                }else {
                    Toast.makeText(AddScheduleActivity.this, "请先登录！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //返回
        findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getU_id()!=null) {
                    Intent intent = new Intent();
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("end", 0);
                    intent.putExtras(bundle1);
                    setResult(RESULT_OK, intent);
                    //更新
                }
                finish();
            }
        });


    }

    public void init() {
        ////准备线程句柄//
        handler = new Handler() {
            public void handleMessage(Message msg) {
                // 如果消息来自子线程
                switch (msg.what) {
                    case 0:
                        //failure
                        Toast.makeText(AddScheduleActivity.this, "添加失败，请重试！", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //success
                        Toast.makeText(AddScheduleActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();

                        //设置本地闹钟管理者
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourint);
                        c.set(Calendar.MINUTE,minuint);
                        //02.确定好选择的时间
                        //03.设置闹钟
                        Bundle bundle = new Bundle();//设置数据
                        bundle.putInt("sche_id",schedule.getSc_no());

                        //04.当之间一到，将执行的响应
                        if(schedule.getSc_mode()==0) {
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            intent.setAction("com.example.zy_alarm_notification.Ring");
                            PendingIntent pendingIntent=PendingIntent.getBroadcast(AddScheduleActivity.this,schedule.getSc_no(),intent,0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
                        }else if(schedule.getSc_mode()==1)
                        {
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            intent.setAction("com.example.zy_alarm_notification.Recom");
                            PendingIntent pendingIntent=PendingIntent.getBroadcast(AddScheduleActivity.this,schedule.getSc_no(),intent,0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
                        }

                        Intent intent = new Intent();
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("end", 1);
                        intent.putExtras(bundle1);
                        setResult(RESULT_OK, intent);
                        //更新
                       finish();
                        break;
                }
            }
        };
    }
    // 以下为返回键退出监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            android.app.AlertDialog isExit = new android.app.AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }
        return false;

    }

    /** 监听对话框里面的button点击事件 */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case android.app.AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case android.app.AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

}
