package com.acotrun.tabFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.acotrun.R;
import com.acotrun.adapter.ListViewAdapter;
import com.acotrun.bean.Data;
import com.acotrun.bean.ImageDownLoader;
import com.acotrun.bean.RoundImageView;
import com.acotrun.activity.AddScheduleActivity;
import com.acotrun.bean.User;
import com.acotrun.utility.Constant;
import com.acotrun.utility.NetInfoUtil;

import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageDownLoader imageDownLoader;
    RoundImageView imageView;
    String uidPic;
    Boolean is_login;
    Button btn_add;
    View homelayout;
    int cnt;
    int firstListItemPosition;
    int lastListItemPosition;
    View vs[];
    ListView lv;
    List<String> list;
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homelayout = inflater.inflate(R.layout.home_layout, container, false);
        Bundle bundle = this.getArguments();
        is_login = bundle.getBoolean("is_login");
        lv = homelayout.findViewById(R.id.lv_list);
        btn_add = homelayout.findViewById(R.id.addSchedule);
        btn_add.setOnClickListener(this);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> ada, View view, int index, long longIndex) {
                lv.showContextMenu();
                return true;
            }
        });

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

        addlist();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                LinkedList<Data> data = new LinkedList<Data>();
                switch (msg.what) {
                    case 0:
                        if (list.size() < 5) {
                            Data d = new Data();
                            d.setText("\t您还未添加计划表哦");
                            data.add(d);
                            break;
                        }
                        for(int i = 0; i < list.size(); i += 5) {
                            Data d = new Data();
                            d.setText("\t"+list.get(i+1)+"-----"+ Constant.ACTIVITY_ITEMS[Integer.parseInt(list.get(i))]);
                            data.add(d);
                        }
                        break;
                    case 1:
                        Data d = new Data();
                        d.setText("\t请尝试登录以获取计划表");
                        data.add(d);
                        break;
                }

                ListViewAdapter lvAdapter = new ListViewAdapter(data, getContext());
                lv.setAdapter(lvAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // 通过getAdapter()方法取得MyAdapter对象，再调用getItem(int)返回一个Data对象
                        Data t = (Data)lv.getAdapter().getItem(i);
//                        // 将点击的item里面的字弹出来
//                        Toast.makeText(getActivity(), t.getText(), Toast.LENGTH_SHORT).show();
                        Bundle bd = new Bundle();
                        String str = t.getText().split("-----")[0].trim();
                        System.out.println("message：" + str);
                        for(int j = 0; j < list.size(); j += 5) {
                            if (list.get(j+1).equals(str)) {
                                bd.putString("kind", list.get(j));
                                bd.putString("name", list.get(j+1));
                                bd.putString("content", list.get(j+2));
                                bd.putString("time", list.get(j+3));
                                bd.putString("uid", list.get(j+4));
                                System.out.println("message：" + list.get(j+1));
                            } else continue;
                        }
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), AddScheduleActivity.class);
                        intent.putExtras(bd);
                        startActivityForResult(intent, 1);
                    }
                });

            }
        };

        return homelayout;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_add) {
            if (is_login) {
                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                startActivityForResult(intent, 1);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Alert!");
                alert.setMessage("请登陆后再使用！");
                alert.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            addlist();
        }
    }

    public void addlist() {
        new Thread() {
            @Override
            public void run() {
                try {
                    User user = User.getInstance();
                    Message msg = new Message();
                    if (user.getU_login()) {
                        String uid = user.getU_id();
                        list = NetInfoUtil.getsche(uid);
                        msg.what = 0;
                    } else msg.what = 1;
                    handler.sendMessage(msg);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}