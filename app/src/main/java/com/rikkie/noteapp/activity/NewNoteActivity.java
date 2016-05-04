package com.rikkie.noteapp.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rikkie.noteapp.config.Log;
import com.rikkie.noteapp.custom.DialogAdapter;
import com.rikkie.noteapp.custom.ImageAdapter;
import com.rikkie.noteapp.custom.LineEdittext;
import com.rikkie.noteapp.db.DatabaseManager;
import com.rikkie.noteapp.db.table.tblNote;
import com.rikkie.noteapp.model.ImageItem;
import com.rikkie.noteapp.model.Item;
import com.rikkie.noteapp.model.Note;
import com.rikkie.noteapp.utils.AlarmReceiver;
import com.rikkie.noteapp.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tuyenpx on 26/04/2016.
 */
public class NewNoteActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout mBgNewNote;

    private TextView mLastModifyTime;
    private LineEdittext mTitleNote;
    private LineEdittext mContentNote;


    private TextView mTextAlarm;
    private Spinner mSpDate;
    private Spinner mSpTime;
    private ImageButton mImageCancel;


    // footer layout
    private LinearLayout mLnFooter;
    private ImageButton mPrivNote;
    private ImageButton mShareNote;
    private ImageButton mDelNote;
    private ImageButton mNextNote;

    private ActionBar mActionBar;


    private boolean isNewNote;
    private static String TAG = NewNoteActivity.class.getName();

    private ArrayList<String> mListDate = new ArrayList<>();
    private ArrayList<String> mListTime = new ArrayList<>();

    private int currentIndex = 0;
    private ArrayList<Note> mArrayListNote = new ArrayList<>();

    private int mColorNote = 0;

    private final static int REQUEST_CAMERA = 1991;
    private final static int SELECT_FILE = 1994;

    private ArrayList<ImageItem> mListImage = new ArrayList<>();
    private ImageAdapter mImageAdapter;
    private GridView mGridViewImage;

    private Note mNote;

    private String mAlarmTimeString = "";

    private AlarmManager mAM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        isNewNote = getIntent().getBooleanExtra(Utils.TAG_BUNDLE, false);
        mBgNewNote = (RelativeLayout) findViewById(R.id.bg_new_note);

        mLastModifyTime = (TextView) findViewById(R.id.tv_lastmodifytime);
        mTitleNote = (LineEdittext) findViewById(R.id.le_title_note);
        mContentNote = (LineEdittext) findViewById(R.id.le_content_note);

        mTextAlarm = (TextView) findViewById(R.id.txt_arlarm);
        mTextAlarm.setOnClickListener(this);
        mAM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mSpDate = (Spinner) findViewById(R.id.sp_date);
        mSpTime = (Spinner) findViewById(R.id.sp_time);

        mSpDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();
                Log.E(TAG, item);

                if (item.equals("Other")) {
                    DateDialog();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mSpTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Log.E(TAG, item);

                if (item.equals("Other")) {
                    TimeDialog();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        mImageCancel = (ImageButton) findViewById(R.id.btn_cancel);
        mImageCancel.setOnClickListener(this);

        mLnFooter = (LinearLayout) findViewById(R.id.footer);
        mPrivNote = (ImageButton) findViewById(R.id.btn_back_n);
        mPrivNote.setOnClickListener(this);
        mShareNote = (ImageButton) findViewById(R.id.btn_share_n);
        mShareNote.setOnClickListener(this);
        mDelNote = (ImageButton) findViewById(R.id.btn_delete_n);
        mDelNote.setOnClickListener(this);
        mNextNote = (ImageButton) findViewById(R.id.btn_next_n);
        mNextNote.setOnClickListener(this);

        mGridViewImage = (GridView) findViewById(R.id.gd_image_note);
        mImageAdapter = new ImageAdapter(getBaseContext(), R.layout.item_image, mListImage);
        mGridViewImage.setAdapter(mImageAdapter);

        boolean isSetAlarm = false;

        if (isNewNote) {
            mSpDate.setVisibility(View.GONE);
            mSpTime.setVisibility(View.GONE);
            mImageCancel.setVisibility(View.GONE);
            mTextAlarm.setVisibility(View.VISIBLE);
            mLnFooter.setVisibility(View.GONE);
            mLastModifyTime.setText(getCurrentTime());
            mActionBar.setTitle("Note");
        } else {
            mLnFooter.setVisibility(View.VISIBLE);
            /**
             * Convert String to Object
             */
            mNote = (Note) getIntent().getSerializableExtra(Utils.TAG_NOTE);
            mColorNote = mNote.getColorNote();
            mArrayListNote = getDatabase();
            isSetAlarm = mNote.isSetAlarm();
            mAlarmTimeString = mNote.getAlarmTimeString();
            for (int i = 0; i < mArrayListNote.size(); i++) {
                if (mArrayListNote.get(i).getIdNote() == mNote.getIdNote()) {
                    currentIndex = i;
                    break;
                }
            }
            setUpNextBackNote(currentIndex);
            Log.E(TAG, mNote.toString());
            setupNote(mNote);
        }

        if (!mAlarmTimeString.equals("") && isSetAlarm) {
            String[] s = mAlarmTimeString.split(";");
            setupCbbDateTime(s[0], s[1]);
        } else {
            setupCbbDateTime(null, null);
        }
    }


    private void setupNote(Note mNote) {
        this.mNote = mNote;
        mColorNote = mNote.getColorNote();

        Log.E(TAG, mNote.toString());

        if (mNote.getTitleNote().length() > 8) {
            mActionBar.setTitle(mNote.getTitleNote().substring(0, 8));
        } else {
            mActionBar.setTitle(mNote.getTitleNote());
        }

        /**
         * process object .
         */

        mTitleNote.setText(mNote.getTitleNote());
        mContentNote.setText(mNote.getContentNote());
        mLastModifyTime.setText(mNote.getLastTimeModifyNote());

        if (mNote.isSetAlarm()) {
            mSpDate.setVisibility(View.VISIBLE);
            mSpTime.setVisibility(View.VISIBLE);
            mImageCancel.setVisibility(View.VISIBLE);
            mTextAlarm.setVisibility(View.GONE);
        }

        // set Image Note :
        if (mListImage.size() > 0 && mNote.getPathImage().trim().equals("")) {
            mListImage.removeAll(mListImage);
            mImageAdapter.notifyDataSetChanged();
        }
        if (!mNote.getPathImage().trim().equals("")) {
            String[] s = mNote.getPathImage().split(";");
            for (int i = 0; i < s.length; i++) {
                mListImage.add(new ImageItem(s[i]));
                mImageAdapter.notifyDataSetChanged();
            }
        }

        switch (mNote.getColorNote()) {
            case 0:
                mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_zero));
                break;
            case 1:
                mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_one));
                break;
            case 2:
                mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_two));
                break;
            case 3:
                mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_three));
                break;
            default:
                mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_zero));
                break;
        }
    }


    private String getCurrentTime() {
        //TODO Need to check deplecate time .
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        return today.monthDay + "/" + (today.month + 1) + "/" + today.year + " " + today.format("%k:%M");
    }


    private ArrayAdapter<String> arrayAdapterDate;
    private ArrayAdapter<String> arrayAdapterTime;

    private void setupCbbDateTime(String dateAlarm, String timeAlarm) {
        if (mListDate.size() > 0) {
            mListDate.removeAll(mListDate);
        }

        if (mListTime.size() > 0) {
            mListTime.removeAll(mListTime);
        }
        /**
         * Setup Value for Dates .
         */
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        if (dateAlarm != null) {
            mListDate.add(dateAlarm);
        }
        mListDate.add("Today");
        mListDate.add("Tomorrow");
        mListDate.add("Next " + weekday_name.substring(0, 3));
        mListDate.add("Other");

        /**
         * Setup Value for Times .
         */

        if (timeAlarm != null) {
            mListTime.add(timeAlarm);
            if (!timeAlarm.equals("09:00")) {
                mListTime.add("09:00");
            }

            if (!timeAlarm.equals("13:00")) {
                mListTime.add("13:00");
            }
            if (!timeAlarm.equals("17:00")) {
                mListTime.add("17:00");
            }
            if (!timeAlarm.equals("20:00")) {
                mListTime.add("20:00");
            }
        } else {
            mListTime.add("09:00");
            mListTime.add("13:00");
            mListTime.add("17:00");
            mListTime.add("20:00");
        }

        mListTime.add("Other");
        arrayAdapterDate = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mListDate);
        arrayAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterTime = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mListTime);
        arrayAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpDate.setAdapter(arrayAdapterDate);
        mSpTime.setAdapter(arrayAdapterTime);
    }

    private void DateDialog() {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mListDate.add(0, dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                mSpDate.setSelection(0);
                arrayAdapterDate.notifyDataSetChanged();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.setTitle("Choose Date");
        dpDialog.show();

    }


    private void TimeDialog() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int houra, int minuteb) {
                mListTime.add(0, houra + ":" + minuteb);
                mSpTime.setSelection(0);
                arrayAdapterTime.notifyDataSetChanged();
            }
        };
        TimePickerDialog dpDialog = new TimePickerDialog(this, listener, hour, minute, true);
        dpDialog.setTitle("Choose Time");
        dpDialog.show();

    }


    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_arlarm:
                mSpDate.setVisibility(View.VISIBLE);
                mSpTime.setVisibility(View.VISIBLE);
                mImageCancel.setVisibility(View.VISIBLE);
                mTextAlarm.setVisibility(View.GONE);
                break;
            case R.id.btn_cancel:
                mSpDate.setVisibility(View.GONE);
                mSpTime.setVisibility(View.GONE);
                mImageCancel.setVisibility(View.GONE);
                mTextAlarm.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_next_n:
                Log.E(TAG, "next currentIndex = " + currentIndex);
                setupNote(mArrayListNote.get(++currentIndex));
                setUpNextBackNote(currentIndex);
                break;
            case R.id.btn_back_n:
                Log.E(TAG, "back currentIndex = " + currentIndex);
                setupNote(mArrayListNote.get(--currentIndex));
                setUpNextBackNote(currentIndex);
                break;
            case R.id.btn_delete_n:
                new AlertDialog.Builder(this)
                        .setTitle("Confirm delete")
                        .setMessage("Do you really want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                getContentResolver().delete(DatabaseManager.URI_NOTE, tblNote._ID + " =?", new String[]{mArrayListNote.get(currentIndex).getIdNote() + ""});
                                mArrayListNote.remove(currentIndex);
                                Log.E(TAG, "mArrayListNote.size = " + mArrayListNote.size() + " currentIndex  = " + currentIndex);
                                if (mArrayListNote.size() > 0) {
                                    if (currentIndex == mArrayListNote.size()) {
                                        currentIndex--;
                                    }
                                    setupNote(mArrayListNote.get(currentIndex));
                                } else {
                                    Intent i = new Intent(getBaseContext(), NewNoteActivity.class);
                                    i.putExtra(Utils.TAG_BUNDLE, true);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case R.id.btn_share_n:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitleNote.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mContentNote.getText().toString());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

        }
    }


    private void setUpNextBackNote(int currentIndex) {

        if (currentIndex == 0) {
            mPrivNote.setEnabled(false);
        } else {
            mPrivNote.setEnabled(true);
        }

        if (currentIndex == mArrayListNote.size() - 1) {
            mNextNote.setEnabled(false);
        } else {
            mNextNote.setEnabled(true);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.E(TAG, "isNewNote = " + isNewNote);
        if (isNewNote) {
            getMenuInflater().inflate(R.menu.add, menu);
        } else {
            getMenuInflater().inflate(R.menu.edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_addnew:
                Intent i = new Intent(getBaseContext(), NewNoteActivity.class);
                i.putExtra(Utils.TAG_BUNDLE, true);
                startActivity(i);
                this.finish();
                break;
            case R.id.action_insert_picture:
                selectImage();
                break;
            case R.id.action_choose_color:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Color");
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_choosecolor, null);
                ImageView color1 = (ImageView) dialogLayout.findViewById(R.id.imv_color1);
                color1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mColorNote = 0;
                        mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_zero));
                        dialog.dismiss();
                    }
                });
                ImageView color2 = (ImageView) dialogLayout.findViewById(R.id.imv_color2);
                color2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mColorNote = 1;
                        mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_one));
                        dialog.dismiss();
                    }
                });
                ImageView color3 = (ImageView) dialogLayout.findViewById(R.id.imv_color3);
                color3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mColorNote = 2;
                        mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_two));
                        dialog.dismiss();
                    }
                });
                ImageView color4 = (ImageView) dialogLayout.findViewById(R.id.imv_color4);
                color4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mColorNote = 3;
                        mBgNewNote.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.color_three));
                        dialog.dismiss();
                    }
                });
                dialog.setView(dialogLayout);
                dialog.show();
                break;
            case R.id.action_done:
                String tileNote = mTitleNote.getText().toString();
                String contentNote = mContentNote.getText().toString();
                String lasttimemodify = getCurrentTime();
                boolean isSetAlarm = (mTextAlarm.getVisibility() == View.VISIBLE) ? false : true;
                if (tileNote.trim().equals("")) {
                    tileNote = "Untitle";
                }
                ContentValues v = new ContentValues();
                v.put(tblNote.TITLE_NOTE, tileNote);
                v.put(tblNote.CONTENT_NOTE, contentNote);
                v.put(tblNote.IS_SET_ALARM, isSetAlarm);
                v.put(tblNote.COLOR_NOTE, mColorNote);
                v.put(tblNote.LASTTIME_MODIFY_NOTE, lasttimemodify);
                v.put(tblNote.ALARM_NOTE, getTimeAlarm());
                v.put(tblNote.IMAGE_NOTE, getListPath());
                v.put(tblNote.ALARM_TIME_STRING, mAlarmTimeString);

                if (isNewNote) {
                    Uri n = getContentResolver().insert(DatabaseManager.URI_NOTE, v);
                    Log.E(TAG, n.toString());
                    int ID = Integer.parseInt(n.toString().replace("content://com.rikkie.noteapp.db/tbl_Note/", ""));

                    if (isSetAlarm) {
                        scheduleAlarm(new Note(ID, getTimeAlarm()));
                    }
                } else {
                    getContentResolver().update(DatabaseManager.URI_NOTE, v, tblNote._ID + " =?", new String[]{mArrayListNote.get(currentIndex).getIdNote() + ""});

                    if (isSetAlarm) {
                        scheduleAlarm(new Note(mNote.getIdNote(), getTimeAlarm()));
                    }
                }
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scheduleAlarm(Note mNote) {
        Intent alarmIntent = new Intent(AlarmReceiver.ACTION_SHOW_NOTIFY);
        alarmIntent.putExtra(Utils.TAG_NOTE, mNote);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mAM.set(AlarmManager.RTC_WAKEUP, mNote.getAlarmNote(), mPendingIntent);
        Log.E(TAG, "Alarm is setted for :  " + mNote.toString());

    }


    private String getListPath() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < mListImage.size() - 1; i++) {
            stringBuilder.append(mListImage.get(i).getPathFile() + ";");
        }
        if (mListImage.size() > 0) {
            stringBuilder.append(mListImage.get(mListImage.size() - 1).getPathFile());
        }

        Log.E(TAG, stringBuilder.toString());
        return stringBuilder.toString();
    }


    private void selectImage() {
        ArrayList<Item> mListItem = new ArrayList<>();
        mListItem.add(new Item("Take Photo", R.drawable.ic_action_camera_dark));
        mListItem.add(new Item("Choose Photo", R.drawable.ic_action_picture));
        DialogAdapter dialogAdapter = new DialogAdapter(getBaseContext(), android.R.layout.select_dialog_item, mListItem);

        new AlertDialog.Builder(this)
                .setTitle("Insert Picture")
                .setCancelable(false)
                .setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    SELECT_FILE);
                        }
                    }
                }).show();
    }


    private long getTimeAlarm() {
        long milliseconds = 0;
        String string_date = "12/5/2016";
        String date = (String) mSpDate.getSelectedItem();
        if (date.equals("Today")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            string_date = sdf.format(c.getTime());
        } else if (date.equals("Tomorrow")) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            string_date = sdf.format(c.getTime());
        } else if (date.contains("Next")) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 7);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            string_date = sdf.format(c.getTime());
        } else {
            string_date = date;
        }
        //2009-06-15T13:45:30
        mAlarmTimeString = string_date.toString() + ";" + mSpTime.getSelectedItem().toString();
//        string_date = string_date + "-" + mSpTime.getSelectedItem().toString();
//        Log.E(TAG, "string_date = " + string_date);

        try {
            //  SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy-hh:ss");
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            Date d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (Exception e) {
            Log.E(TAG, e.toString());

        }

        String[] s = mSpTime.getSelectedItem().toString().split(":");

        milliseconds = milliseconds + Integer.parseInt(s[0]) * 60 * 60 * 1000 + Integer.parseInt(s[1]) * 60 * 1000;

        Log.E(TAG, "milliseconds = " + milliseconds);

        return milliseconds;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mListImage.add(new ImageItem(destination.getPath()));
                mImageAdapter.notifyDataSetChanged();

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                mListImage.add(new ImageItem(selectedImagePath));
                mImageAdapter.notifyDataSetChanged();
            }

        }
    }
}
