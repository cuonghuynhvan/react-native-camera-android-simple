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

        List<Camera.Size> supportedSizes = parameters.getSupportedPictureSizes();
        Camera.Size pictureSize = getBestSize(supportedSizes, 0);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);

        float whRatio = (float) pictureSize.width / pictureSize.height;

        List<Camera.Size> previewSupportedSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = getBestSize(previewSupportedSizes, whRatio);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        boolean hasAutoFocus = supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);

        if(hasAutoFocus) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        List<String> supportedScreenModes = camera.getParameters().getSupportedSceneModes();
        boolean hasAutoScene = supportedScreenModes != null && supportedFocusModes.contains(Camera.Parameters.SCENE_MODE_AUTO);
        if(hasAutoScene) {
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        }

        parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);

        int orientation = cameraInfo.orientation;
        parameters.setRotation(orientation);

        camera.setParameters(parameters);
    }

    public static Camera.Size getBestSize(List<Camera.Size> supportedSizes, float whRatio) {
        Camera.Size bestSize = null;
        for (Camera.Size size : supportedSizes) {
            float ratio = (float) size.width / size.height;

            if( whRatio != 0 && ratio != whRatio) {
                continue;
            }

            if (bestSize == null) {
                bestSize = size;
                continue;
            }

            int resultArea = bestSize.width * bestSize.height;
            int newArea = size.width * size.height;

            if (newArea > resultArea) {
                bestSize = size;
            }
        }

        return bestSize;
    }

    public Camera.Parameters getCameraParameters() {
        return mCamera.getParameters();
    }

    public Camera getCurrentCamera() {
        return mCamera;
    }
}
