package com.daten.runtimedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    //请求相机权限ID
    private static final int REQUEST_CAMERA = 0;
    //请求联系人权限ID
    private static final int REQUEST_CONTACTS = 1;

    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    private View mLayout;

    public void showCamera(View view) {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        //检查是否已经获取到相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 相机权限没有获取
            requestCameraPermission();

        } else {
            Log.i(TAG,
                    "CAMERA permission has already been granted. Displaying camera preview.");
            showCameraPreview();
        }
    }

    private void showCameraPreview() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment,CameraPreviewFragment.newInstance())
                .addToBackStack("contacts")
                .commit();
    }

    /**
     * 获取相机权限
     */
    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.sample_main_layout);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RuntimePermissionsFragment fragment = new RuntimePermissionsFragment();
            transaction.add(R.id.sample_content_fragment,fragment);
            transaction.commit();
        }
    }
}
