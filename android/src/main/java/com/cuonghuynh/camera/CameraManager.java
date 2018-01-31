package com.cuonghuynh.camera;

import android.graphics.ImageFormat;
import android.hardware.Camera;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cuong.huynh on 5/31/17.
 */

public class CameraManager {
    public static final int TYPE_CAMERA_FRONT = 1;
    public static final int TYPE_CAMERA_BACK = 2;

    private static CameraManager mInstance;

    private HashMap<Integer, Integer> mTypeToIndex = new HashMap<>();
    private HashMap<Integer, Camera.CameraInfo> mCameraInfoHashMap = new HashMap<>();

    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;

    public static CameraManager getInstance() {
        if (mInstance == null) {
            mInstance = new CameraManager();
        }

        return mInstance;
    }

    private CameraManager() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            mCameraInfoHashMap.put(i, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mTypeToIndex.put(TYPE_CAMERA_BACK, i);
            } else {
                mTypeToIndex.put(TYPE_CAMERA_FRONT, i);
            }
        }
    }

    public Camera getCameraInstance(int type) {
        Camera c = null;
        try {
            int idx = mTypeToIndex.get(type);
            c = Camera.open(idx); // attempt to get a Camera instance
            mCamera = c;
            mCameraInfo = mCameraInfoHashMap.get(idx);
            setDefaultCameraParameters(mCamera, mCameraInfo);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void setDefaultCameraParameters(Camera camera, Camera.CameraInfo cameraInfo) {
        Camera.Parameters parameters = camera.getParameters();

        parameters.setPictureFormat(ImageFormat.JPEG);

        int orientation = cameraInfo.orientation;
        parameters.setRotation(orientation);

        List<Camera.Size> supportedSizes = parameters.getSupportedPictureSizes();
        List<Camera.Size> previewSupportedSizes = parameters.getSupportedPreviewSizes();

        Camera.Size previewSize = null, pictureSize = null;

        for (int i = supportedSizes.size() - 1; i >= 0; i--) {
            Camera.Size tempSize = supportedSizes.get(i);
            Camera.Size tempPreviewSize = null;
            float whRatio = (float) tempSize.width / tempSize.height;

            for (int j = previewSupportedSizes.size() - 1; j >= 0; j--) {
                Camera.Size size = previewSupportedSizes.get(j);
                float tempWHRatio = (float) size.width / size.height;
                if (tempWHRatio == whRatio) {
                    if (tempPreviewSize == null || isNewSizeLarger(tempPreviewSize, size)) {
                        tempPreviewSize = size;
                    }
                }
            }

            if (tempPreviewSize != null) {
                if (pictureSize == null || isNewSizeLarger(pictureSize, tempSize)) {
                    pictureSize = tempSize;
                    previewSize = tempPreviewSize;
                }
            }
        }

        if (pictureSize == null) {
            pictureSize = supportedSizes.get(supportedSizes.size() - 1);
            previewSize = previewSupportedSizes.get(previewSupportedSizes.size() - 1);
        }

        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        boolean hasAutoFocus = supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);

        if (hasAutoFocus) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        List<String> supportedScreenModes = camera.getParameters().getSupportedSceneModes();
        boolean hasAutoScene = supportedScreenModes != null && supportedFocusModes.contains(Camera.Parameters.SCENE_MODE_AUTO);
        if (hasAutoScene) {
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        }

        parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);

        camera.setParameters(parameters);
    }

    private boolean isNewSizeLarger(Camera.Size oldSize, Camera.Size newSize) {
        int resultArea = oldSize.width * oldSize.height;
        int newArea = newSize.width * newSize.height;

        return newArea > resultArea;
    }

    public Camera.Parameters getCameraParameters() {
        return mCamera.getParameters();
    }

    public Camera getCurrentCamera() {
        return mCamera;
    }
}
