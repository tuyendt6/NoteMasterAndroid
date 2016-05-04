package com.rikkie.noteapp.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkie.noteapp.activity.NewNoteActivity;
import com.rikkie.noteapp.activity.R;
import com.rikkie.noteapp.config.Log;
import com.rikkie.noteapp.model.Note;
import com.rikkie.noteapp.utils.Utils;

import java.util.ArrayList;

/**
 * Created by tuyenpx on 26/04/2016.
 */
public class MainNoteAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> mListNotes;
    private Context mContext;

    private static final String TAG = MainNoteAdapter.class.getSimpleName();


    public MainNoteAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        mListNotes = objects;
        mContext = context;
    }


    static class ViewHolder {
        TextView tvTitleNote;
        ImageView imAlarmNote;
        TextView tvContentNode;
        TextView tvLastTimeModify;
        RelativeLayout bgNote;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_note, null);
            holder = new ViewHolder();
            holder.tvTitleNote = (TextView) v.findViewById(R.id.tv_titleNote);
            holder.imAlarmNote = (ImageView) v.findViewById(R.id.imv_alarm);
            holder.tvContentNode = (TextView) v.findViewById(R.id.tv_content);
            holder.tvLastTimeModify = (TextView) v.findViewById(R.id.tv_time);
            holder.bgNote = (RelativeLayout) v.findViewById(R.id.bg_note);
            holder.bgNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.E(TAG, "mGridViewNote onItemClick");
                    Note myNote = mListNotes.get(position);
                    Log.E(TAG, myNote.toString());
                    Intent k = new Intent(mContext, NewNoteActivity.class);
                    k.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    k.putExtra(Utils.TAG_BUNDLE, false);
                    k.putExtra(Utils.TAG_NOTE, myNote);
                    mContext.startActivity(k);
                }
            });
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Note myNote = mListNotes.get(position);

        holder.tvTitleNote.setText(myNote.getTitleNote());
        if (myNote.isSetAlarm()) {
            holder.imAlarmNote.setVisibility(View.VISIBLE);
        } else {
            holder.imAlarmNote.setVisibility(View.GONE);
        }
        holder.tvContentNode.setText(myNote.getContentNote());
        holder.tvLastTimeModify.setText(myNote.getLastTimeModifyNote());

        switch (myNote.getColorNote()) {
            case 0:
                // holder.bgNote.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_zero));

                if (Build.VERSION.SDK_INT >= 16) {
                    holder.bgNote.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_zero));
                } else {
                    holder.bgNote.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_zero));
                }

                break;
            case 1:
                //  holder.bgNote.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_one));
                if (Build.VERSION.SDK_INT >= 16) {
                    holder.bgNote.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_one));
                } else {
                    holder.bgNote.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_one));
                }
                break;
            case 2:
                //  holder.bgNote.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_two));
                if (Build.VERSION.SDK_INT >= 16) {
                    holder.bgNote.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_two));
                } else {
                    holder.bgNote.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_two));
                }
                break;
            case 3:
                //   holder.bgNote.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_three));
                if (Build.VERSION.SDK_INT >= 16) {
                    holder.bgNote.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_three));
                } else {
                    holder.bgNote.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_three));
                }
                break;
            default:
                //  holder.bgNote.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_zero));
                if (Build.VERSION.SDK_INT >= 16) {
                    holder.bgNote.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_zero));
                } else {
                    holder.bgNote.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shadow_item_note_zero));
                }
                break;
        }
        return v;
    }


}
