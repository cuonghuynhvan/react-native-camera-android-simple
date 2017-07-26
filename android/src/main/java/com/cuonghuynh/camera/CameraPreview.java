package com.cuonghuynh.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by cuong.huynh on 5/31/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private int mCameraType = CameraManager.TYPE_CAMERA_BACK;

    private int mPreviewWidth = 0, mPreviewHeight;

    public CameraPreview(Context context, int initType) {
        super(context);

        mCameraType = initType;
        initCamera();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initCamera() {
        mCamera = CameraManager.getInstance().getCameraInstance(mCameraType);

        if(mCamera != null) {
            mCamera.setDisplayOrientation(90);

            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();

            //cause the camera display orientation is 90 degree.
            mPreviewWidth = previewSize.height;
            mPreviewHeight = previewSize.width;
        } else {
            mPreviewHeight = 1;
            mPreviewWidth = 1;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mPreviewWidth || 0 == mPreviewHeight) {
            setMeasuredDimension(width, height);
        } else {
            int rWidth;
            int rHeight;
            if (width < height * mPreviewWidth / mPreviewHeight) {
                rWidth = height * mPreviewWidth / mPreviewHeight;
                rHeight = height;
            } else {
                rWidth = width;
                rHeight = width * mPreviewHeight / mPreviewWidth;
            };
            setMeasuredDimension(rWidth, rHeight);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    public void startPreview() {
        if(mCamera == null) {
            initCamera();
        }

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("Camera Preview", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void changeCameraType(int type) {
        if(mCameraType != type) {
            mCameraType = type;
            releaseCamera();
            startPreview();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        releaseCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("CameraPreview", "Error starting camera preview: " + e.getMessage());
        }
    }

    public double getRatio() {
        if(mPreviewWidth == 0) {
            throw new RuntimeException("PreviewWidth cannot be zero");
        }

        return (float) mPreviewWidth / mPreviewHeight;
    }

    public void autoFocus() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

            }
        });
    }
}
