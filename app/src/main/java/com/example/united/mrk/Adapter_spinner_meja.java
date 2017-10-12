package com.example.united.mrk;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by united on 31/08/2017.
 */
public class Adapter_spinner_meja extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    private ArrayList<Data_meja> rvsubmenu;

    public Adapter_spinner_meja(Context applicationContext,  ArrayList<Data_meja> inputData) {
        this.context = applicationContext;
        this.rvsubmenu = inputData;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return rvsubmenu.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) convertView.findViewById(R.id.nama_meja);
        TextView fpkey = (TextView) convertView.findViewById(R.id.fpkey);
        TextView ftablekey = (TextView) convertView.findViewById(R.id.ftable_key);
        TextView ftype = (TextView) convertView.findViewById(R.id.ftype);
        final Data_meja ds = rvsubmenu.get(position);
        names.setText(ds.getnama_meja());
        fpkey.setText(ds.getfpkey());
        ftablekey.setText(ds.getftablekey());
        ftype.setText(ds.getftype());
        return convertView;
    }




}