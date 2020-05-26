package com.acotrun.utility;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.acotrun.bean.User;

public class DatabaseOperation {
    public static boolean flag;

    public DatabaseOperation() {
    }

    // 连接数据库
    public void create_db() {
    }

    //插入日记信息到数据库
    public static void insert_db(final String name, final String creator, final String _class, final String content, final String time) {
        new Thread() {
            @Override
            public void run() {
                try {
                    flag = NetInfoUtil.addActivity(name, creator, _class, content, time);
                    if (flag)
                        Log.d("-----------", "okkkkk");
                    else Log.d("-----------", "failllll");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
//    //根据id更新数据库内容信息
//    public void update_db(String title, String text, String time,
//                          String datatype, String datatime, String locktype, String lock,
//                          int item_ID) {
//        if (text.isEmpty()) {
//            Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
//        } else {
//            db.execSQL("update notes set context='" + text + "',title='"
//                    + title + "',time='" + time + "',datatype='" + datatype
//                    + "',datatime='" + datatime + "',locktype='" + locktype
//                    + "',lock='" + lock + "'where _id='" + item_ID + "'");
//            Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
//        }
//    }
//    //查询所有日记内容
//    public Cursor query_db() {
//        Cursor cursor = db.rawQuery("select * from notes", null);
//        return cursor;
//    }
//    //根据数据id查询数据内容
//    public Cursor query_db(int item_ID) {
//        Cursor cursor = db.rawQuery("select * from notes where _id='" + item_ID
//                + "';", null);
//        return cursor;
//
//    }
//
//    // select * from 表名 where name like '%abc%'//模糊查询
//    public Cursor query_db(String keword) {
//        Cursor cursor = db.rawQuery("select * from notes where context like '%"
//                + keword + "%';", null);
//        return cursor;
//    }
//
//    // select * from 表名 where 时间 between '开始时间' and '结束时间'//时间段查询
//    public Cursor query_db(String starttime, String endtime) {
//        Cursor cursor = db.rawQuery("select * from notes where time >='" + starttime + "'and time<='"
//                + endtime + "';", null);
//        return cursor;
//    }
//
//    // 删除某一条数据
//    public void delete_db(int item_ID) {
//        db.execSQL("delete from notes where _id='" + item_ID + "'");
//    }

    // 关闭数据库
    public void close_db() {
    }
}
