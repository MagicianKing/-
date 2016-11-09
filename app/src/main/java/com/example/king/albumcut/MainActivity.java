package com.example.king.albumcut;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_CAMERA_ONLY = 100;
    private static final int RESULT_CAMERA_CROP_PATH_RESULT = 301;
    private ImageView mImage;
    private Uri imageUri;
    private Uri imageCropUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/temp.jpg");
        imageUri = Uri.fromFile(file);
        File cropFile = new File(path + "/temp_crop.jpg");
        imageCropUri = Uri.fromFile(cropFile);

        mImage = (ImageView) findViewById(R.id.img_albm);
        Button btn_take_camera_only = (Button) findViewById(R.id.button);
        btn_take_camera_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeCameraOnly();
            }
        });
    }

    private void takeCameraOnly() {
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RESULT_CAMERA_ONLY);
    }

    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RESULT_CAMERA_CROP_PATH_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case RESULT_CAMERA_ONLY: {
                cropImg(imageUri);
            }
            break;
            case RESULT_CAMERA_CROP_PATH_RESULT: {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageCropUri));
                        mImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

}
