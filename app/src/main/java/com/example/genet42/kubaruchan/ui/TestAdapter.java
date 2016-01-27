package com.example.genet42.kubaruchan.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genet42.kubaruchan.R;

/**
 *
 */
public class TestAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    public TestAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setText(String.format("%d", position));
        } else {
            textView = (TextView) convertView;
        }
        return textView;
    }
}
