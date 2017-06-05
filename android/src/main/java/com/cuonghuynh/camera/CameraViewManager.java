package com.cuonghuynh.camera;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by cuong.huynh on 5/31/17.
 */

public class CameraViewManager extends ViewGroupManager<CameraView> {
    @Override
    public String getName() {
        return "AndroidCameraView";
    }

    @Override
    protected CameraView createViewInstance(ThemedReactContext reactContext) {
        return new CameraView(reactContext);
    }

    @ReactProp(name = "type")
    public void setType(CameraView view, String type) {
        view.changeCameraType(type.equals("front") ? 1 : 2);
    }
}
