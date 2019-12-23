package com.acotrun.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acotrun.R;
import com.acotrun.bean.Data;

import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter {
    private LinkedList<Data> mDataList;
    private Context mContext;

    // 构造函数
    public ListViewAdapter(LinkedList<Data> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        view = LayoutInflater.from(this.mContext).inflate(R.layout.list_view_item, viewGroup, false);
        TextView text = (TextView) view.findViewById(R.id.text_item);
        text.setText(mDataList.get(i).getText());
        holder.setTextView(text);
        view.setTag(holder);
        return view;
    }
    class ViewHolder{
        private TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }
}
