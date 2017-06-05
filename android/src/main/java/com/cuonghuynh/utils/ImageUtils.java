package com.cuonghuynh.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;

/**
 * Created by cuong.huynh on 6/2/17.
 */

public class ImageUtils {

    public static Size getImageSize(String path) {
        // get the real image resolution
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        Size size = new Size(opts.outWidth, opts.outHeight);
        return size;
    }

    public static Bitmap cropImage(String path, int top, int left, int right, int bottom) throws IOException {
        // only accept file path.
        if(path.startsWith("file:/")) {
            path = path.substring("file:/".length());
        }
        BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(path, false);
        Rect rect = new Rect(left, top, right, bottom);
        return regionDecoder.decodeRegion(rect, new BitmapFactory.Options());
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotation) {
        boolean reverseWidthHeight = ((rotation / 90) % 2) == 1;

        int width = reverseWidthHeight ? bitmap.getHeight() : bitmap.getWidth();
        int height = reverseWidthHeight ? bitmap.getWidth() : bitmap.getHeight();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap newBitmap = Bitmap.createBitmap(width, height, conf);
        Canvas canvas = new Canvas(newBitmap);

        Matrix matrix = new Matrix();
        matrix.setRotate(rotation, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        matrix.postTranslate(
                canvas.getWidth() / 2f - bitmap.getWidth() / 2f,
                canvas.getHeight() / 2f - bitmap.getHeight() / 2f
        );

        canvas.drawBitmap(bitmap, matrix, new Paint());

        return newBitmap;
    }

    public static int getCameraPhotoOrientation(String imagePath){
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static class Size {
        public int width;
        public int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
