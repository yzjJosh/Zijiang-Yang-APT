package com.josh.connexus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


public class UploadPreview extends Activity {

    private String filePath;
    private long streamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_preview);
        filePath = getIntent().getStringExtra("path");
        streamId = getIntent().getLongExtra("streamId", -1);
        ((TextView)findViewById(R.id.upload_preview_title)).setText(getIntent().getStringExtra("streamName"));
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Matrix matrix = new Matrix();
        try {
            ExifInterface ei = new ExifInterface(filePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        ImageView imageView = (ImageView) findViewById(R.id.upload_picture_preview);
        imageView.setImageBitmap(bitmap);

    }



    public void onUploadClick(View v) {
        Intent intent = new Intent(this, UploadingService.class);
        intent.putExtra("path", filePath);
        intent.putExtra("streamId", streamId);
        startService(intent);
        finish();
    }

    public void onCancelClick(View v){
        finish();
    }


}
