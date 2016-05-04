package com.rikkie.noteapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.rikkie.noteapp.custom.MainNoteAdapter;
import com.rikkie.noteapp.model.Note;
import com.rikkie.noteapp.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ArrayAdapter<Note> mArrayAdapter;
    private ArrayList<Note> mListNotes = new ArrayList<>();
    private GridView mGridViewNote;
    private static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mGridViewNote = (GridView) findViewById(R.id.gv_note);
        if (mListNotes.size() > 0) {
            mListNotes.removeAll(mListNotes);
        }
        mListNotes = getDatabase();
        mArrayAdapter = new MainNoteAdapter(getBaseContext(), R.layout.item_note, mListNotes);
        mGridViewNote.setAdapter(mArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_addnew:
                Intent i = new Intent(getBaseContext(), NewNoteActivity.class);
                i.putExtra(Utils.TAG_BUNDLE, true);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
