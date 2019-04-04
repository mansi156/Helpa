package com.helpa.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;


import com.helpa.interfaces.ImageCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by appinventiv on 27/10/17.
 */

public class TakeImage {

    public static TakeImage getImage;
    private Activity activity;
    private Uri mImageCaptureUri;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 0x003;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 0x005;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0x004;
    public static final int CAMERA_IMAGE_INTENT = 0x001;
    public static final int GALLERY_IMAGE_INTENT = 0x002;
    private ImageCallback callback;

    public TakeImage(Activity activity,ImageCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public static TakeImage getInstance(Activity mActivity, ImageCallback callback)
    {
        getImage = new TakeImage(mActivity,callback);
        return getImage;
    }


    public void fromCamera() {
        if (hasPermission(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA)) {
            if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL)) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        File file=createImageFile();
                        mImageCaptureUri=FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mImageCaptureUri = Uri.fromFile(new File(getFilePath()));
                }
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                try {
                    intentCamera.putExtra("return-data", true);
                    activity.startActivityForResult(intentCamera, CAMERA_IMAGE_INTENT);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    public void fromGallery() {
        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, GALLERY_IMAGE_INTENT);
        }
    }


    public boolean hasPermission(String permission, int reqId) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        if (result == PackageManager.PERMISSION_GRANTED) return true;
        else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission}, reqId);
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fromCamera();
                } else {
                    Toast.makeText(activity, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fromCamera();
                } else {
                    Toast.makeText(activity, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fromGallery();
                } else {
                    Toast.makeText(activity, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_IMAGE_INTENT) {
                if (isSdCardAvailable()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                    } else {
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                    }
                }
                callback.onSuccess(mImageCaptureUri);
            } else if (requestCode == GALLERY_IMAGE_INTENT) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                File file = new File(picturePath);
                mImageCaptureUri = Uri.fromFile(file);
                callback.onSuccess(mImageCaptureUri);
            }
        }
    }

    public String getFilePath() {
        File file = new File(AppConstant.APP_IMAGE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public boolean isSdCardAvailable() {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            return true;
        } else return false;
    }
}
