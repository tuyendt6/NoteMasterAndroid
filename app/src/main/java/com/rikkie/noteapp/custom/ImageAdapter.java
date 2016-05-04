package com.rikkie.noteapp.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.rikkie.noteapp.activity.R;
import com.rikkie.noteapp.config.Log;
import com.rikkie.noteapp.model.ImageItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tuyenpx on 28/04/2016.
 */
public class ImageAdapter extends ArrayAdapter<ImageItem> {

    private Context mContext;
    private ArrayList<ImageItem> mListImageItem;
    private static final String TAG = ImageAdapter.class.getSimpleName();


    public ImageAdapter(Context context, int resource, ArrayList<ImageItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mListImageItem = objects;
    }

    static class ViewHolder {
        ImageView mThumbnail;
        ImageButton mCancel;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_image, null);
            holder = new ViewHolder();
            holder.mThumbnail = (ImageView) v.findViewById(R.id.btn_thumbnail);
            holder.mThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPicture(mListImageItem.get(position).getPathFile());
                }
            });
            holder.mCancel = (ImageButton) v.findViewById(R.id.btn_delete_image);
            holder.mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListImageItem.remove(position);
                    notifyDataSetChanged();
                }
            });
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.mThumbnail.setImageBitmap(processPicture(mListImageItem.get(position).getPathFile()));
        return v;
    }

    private void openPicture(String path) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "image/*");
        mContext.startActivity(intent);
    }

    private Bitmap processPicture(String path) {
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        try {
            ExifInterface exif = new ExifInterface(new File(path).getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.D("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
            Log.E(TAG, "Exception: " + e.toString());
        }
        return bm;
    }
}
