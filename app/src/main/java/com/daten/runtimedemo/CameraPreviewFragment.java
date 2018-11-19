package com.daten.runtimedemo;


import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraPreviewFragment extends Fragment {

    private static final String TAG = "CameraPreview";
    private static final int CAMERA_ID = 0;

    private CameraPreview mPreview;
    private Camera mCamera;

    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId);
        } catch (Exception e) {
            Log.d(TAG, "Camera " + cameraId + " is not available: " + e.getMessage());
        }
        return c;
    }

    public static Fragment newInstance() {
        return new CameraPreviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCamera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;
        //相机可用
        if (mCamera != null) {
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }
        //相机不可用
        if (mCamera == null || cameraInfo == null) {
            // 相机不可用，显示错误提示
            Toast.makeText(getActivity(), "Camera is not available.", Toast.LENGTH_SHORT).show();
            return inflater.inflate(R.layout.fragment_camera_unavailable, null);
        }

        View root = inflater.inflate(R.layout.fragment_camera, null);
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay()
                .getRotation();
        mPreview = new CameraPreview(getActivity(), mCamera, cameraInfo, displayRotation);
        FrameLayout preview = root.findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        return root;
    }

}
