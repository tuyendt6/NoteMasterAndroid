package com.rikkie.noteapp.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.rikkie.noteapp.db.DatabaseManager;
import com.rikkie.noteapp.db.table.tblNote;
import com.rikkie.noteapp.model.Note;

import java.util.ArrayList;

/**
 * Created by tuyenpx on 26/04/2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected ArrayList<Note> getDatabase() {
        ArrayList<Note> mList = new ArrayList<>();
        Cursor c = getContentResolver().query(DatabaseManager.URI_NOTE, null, null, null, tblNote.LASTTIME_MODIFY_NOTE);
        while (c.moveToNext()) {
            Note note = new Note(c.getInt(c.getColumnIndex(tblNote._ID)), c.getString(c.getColumnIndex(tblNote.TITLE_NOTE)), c.getString(c.getColumnIndex(tblNote.CONTENT_NOTE)), c.getInt(c.getColumnIndex(tblNote.COLOR_NOTE)), c.getString(c.getColumnIndex(tblNote.LASTTIME_MODIFY_NOTE)), c.getLong(c.getColumnIndex(tblNote.ALARM_NOTE)), c.getInt(c.getColumnIndex(tblNote.IS_SET_ALARM)) == 1, c.getString(c.getColumnIndex(tblNote.IMAGE_NOTE)), c.getString(c.getColumnIndex(tblNote.ALARM_TIME_STRING)));
            mList.add(note);
        }
        c.close();
        return mList;
    }
}
