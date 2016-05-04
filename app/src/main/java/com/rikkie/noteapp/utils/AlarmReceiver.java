package com.rikkie.noteapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.rikkie.noteapp.activity.NewNoteActivity;
import com.rikkie.noteapp.activity.R;
import com.rikkie.noteapp.config.Log;
import com.rikkie.noteapp.db.DatabaseManager;
import com.rikkie.noteapp.db.table.tblNote;
import com.rikkie.noteapp.model.Note;

/**
 * Created by tuyenpx on 29/04/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String ACTION_SHOW_NOTIFY = "com.rikkie.setup_Alarm";
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Note notifyNote = (Note) intent.getSerializableExtra(Utils.TAG_NOTE);
        Log.E(TAG, "alarm is received and check for " + notifyNote.toString());
        int ID = notifyNote.getIdNote();
        long time = notifyNote.getAlarmNote();
        Cursor c = context.getContentResolver().query(DatabaseManager.URI_NOTE, null, tblNote._ID + " =?", new String[]{ID + ""}, null);

        if (c.getCount() < 1) {
            c.close();
            return;
        } else {
            c.moveToFirst();
            Note note = new Note(c.getInt(c.getColumnIndex(tblNote._ID)), c.getString(c.getColumnIndex(tblNote.TITLE_NOTE)), c.getString(c.getColumnIndex(tblNote.CONTENT_NOTE)), c.getInt(c.getColumnIndex(tblNote.COLOR_NOTE)), c.getString(c.getColumnIndex(tblNote.LASTTIME_MODIFY_NOTE)), c.getLong(c.getColumnIndex(tblNote.ALARM_NOTE)), c.getInt(c.getColumnIndex(tblNote.IS_SET_ALARM)) == 1, c.getString(c.getColumnIndex(tblNote.IMAGE_NOTE)), c.getString(c.getColumnIndex(tblNote.ALARM_TIME_STRING)));
            c.close();
            if (note.isSetAlarm() && time == note.getAlarmNote()) {
                showNotify(context, note);
            }
        }
    }

    private void showNotify(Context context, Note note) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle(note.getTitleNote());
        mBuilder.setContentText(note.getContentNote());

        Intent resultIntent = new Intent(context, NewNoteActivity.class);
        resultIntent.putExtra(Utils.TAG_BUNDLE, false);
        resultIntent.putExtra(Utils.TAG_NOTE, note);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NewNoteActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(note.getIdNote(), mBuilder.build());

    }


}
