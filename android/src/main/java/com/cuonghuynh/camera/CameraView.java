package com.cuonghuynh.camera;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by cuong.huynh on 5/31/17.
 */

public class CameraView extends ViewGroup {
    private static final String TAG = CameraView.class.getSimpleName();

    private CameraPreview mPreview;
    private int mInitType = CameraManager.TYPE_CAMERA_BACK;

    public CameraView(Context context) {
        super(context);
    }

    public void startCamera() {
        if (null == this.mPreview) {
            mPreview = new CameraPreview(getContext(), mInitType);
            addView(mPreview);
            requestLayout();
        }
    }

    public void changeCameraType(int type) {
        if(mPreview != null) {
            mPreview.changeCameraType(type);
        } else {
            mInitType = type;
        }

        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        startCamera();
        layoutViewFinder(l, t, r, b);
    }

    private void layoutViewFinder(int left, int top, int right, int bottom) {
        if (null == mPreview) {
            return;
        }
        float width = right - left;
        float height = bottom - top;
        int viewfinderWidth;
        int viewfinderHeight;
        double ratio;
        ratio = this.mPreview.getRatio();

        if(ratio == 0) {
            this.mPreview.layout(0, 0, (int) width, (int) height);
            return;
        }

        if (ratio * height < width) {
            viewfinderHeight = (int) (width / ratio);
            viewfinderWidth = (int) width;
        } else {
            viewfinderWidth = (int) (ratio * height);
            viewfinderHeight = (int) height;
        }

        int viewFinderPaddingX = (int) ((width - viewfinderWidth) / 2);
        int viewFinderPaddingY = (int) ((height - viewfinderHeight) / 2);

        this.mPreview.layout(viewFinderPaddingX, viewFinderPaddingY, viewFinderPaddingX + viewfinderWidth, viewFinderPaddingY + viewfinderHeight);
        this.postInvalidate(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }

    private void autoFocus() {
        mPreview.autoFocus();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            autoFocus();
        }
        return true;
    }
}
