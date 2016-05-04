package com.rikkie.noteapp.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rikkie.noteapp.model.Item;

import java.util.ArrayList;

/**
 * Created by tuyenpx on 28/04/2016.
 */

public class DialogAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private ArrayList<Item> mListItem;

    public DialogAdapter(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mListItem = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView tv = (TextView) v.findViewById(android.R.id.text1);
        tv.setCompoundDrawablesWithIntrinsicBounds(mListItem.get(position).icon, 0, 0, 0);
        int dp5 = (int) (5 * mContext.getResources().getDisplayMetrics().density + 0.5f);
        tv.setCompoundDrawablePadding(dp5);
        return v;
    }
}
