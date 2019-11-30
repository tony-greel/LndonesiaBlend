package com.example.lndonesiablend.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

/**
 * Created by Sikang on 2017/7/11.
 */

public class BitmapUtils {
    /**
     * Bitmap裁剪
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int left, int top, int width, int height) {
        if (null == bitmap || width <= 0 || height < 0) {
            return null;
        }
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg >= width && heightOrg >= height) {
            try {
                bitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
            } catch (Exception e) {
                return null;
            }
        }
        return bitmap;
    }


    /**
     * 缩放图片到指定大小（保持比例）
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int w, int h) {
        if (w == 0 || h == 0)
            return bitmap;
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        if (w == width && h == height)
            return bitmap;

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }


    /**
     * 从SD卡中加载图片中心区域
     */
    public static Bitmap loadBitmapCenterByPath(String path, int width, int height) {
        Bitmap bitmap = loadBitmapByPath(path, width, height);//先按比例缩小到最小倍数，然后加载图片到内存
        bitmap = scaleBitmap(bitmap, width, height);//再将加载好的图片缩放到指定大小
        return cropBitmapCenter(bitmap, width, height);
    }


    /**
     * 按指定尺寸加载SD卡 中的图片(指定宽高保持比例缩放)
     */
    public static Bitmap loadBitmapByPath(String path, float width, float height) {
        int degree = readPictureDegree(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(path, options);
        float outWidth = options.outWidth;
        float outHeight = options.outHeight;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;

        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int scaleX = (int) (outWidth / width);
            int scaleY = (int) (outHeight / height);
            options.inSampleSize = scaleX < scaleY ? scaleX : scaleY;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        //把图片旋转为正的方向
        return rotateBitmap(bitmap, degree);
    }


    /**
     * 取中间区域
     */
    public static Bitmap cropBitmapCenter(Bitmap bitmap, int width, int height) {
        if (bitmap == null || width <= 0 || height <= 0) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w <= width && h <= height)
            return bitmap;
        if (width > w)
            width = w;
        if (height > h)
            height = h;

        int top, left;
        if (w <= width)
            left = 0;
        else
            left = (w - width) / 2;


        if (h <= height)
            top = 0;
        else
            top = (h - height) / 2;

        return cropBitmap(bitmap, left, top, width, height);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 保存图片到本地
     *
     * @param quality 质量压缩比例
     */
    public static File saveBitmapToSDCard(Bitmap mBitmap, File file, int quality) {
        if (mBitmap == null)
            return null;
        try {
            if (file.exists())
                file.delete();
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists())
            return file;
        return null;
    }

    /**
     * 将相机图片转为横向
     */
    public static Bitmap getHorizontalPhoto(Bitmap bitmap) {
        if (bitmap.getWidth() < bitmap.getHeight()) {
            return rotateBitmap(bitmap, -90);
        }
        return bitmap;
    }

    /**
     * 图片旋转
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
    }


    /**
     * 将图片数据压缩，并转为bitmap
     */
    public static Bitmap getBitmapWithMaxLimit(byte[] data, final int maxSquerSize, final int maxFileSize) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        SoftReference<Bitmap> temp = new SoftReference<>(BitmapFactory.decodeStream(byteArrayInputStream, null, opts));

        SoftReference<Bitmap> result = temp;

        final float BITMAP_MAX_SQUARE_SIZE_FLOAT = maxSquerSize * 1.0f;

        if (temp.get().getWidth() > maxSquerSize || temp.get().getHeight() > maxSquerSize) {
            //缩小到框内
            float proportion = BITMAP_MAX_SQUARE_SIZE_FLOAT / Math.max(temp.get().getWidth(), temp.get().getHeight());

            int outWidth = ((int) (temp.get().getWidth() * proportion));
            int outHeight = ((int) (temp.get().getHeight() * proportion));

            result = new SoftReference<>(Bitmap.createScaledBitmap(temp.get(), outWidth, outHeight, true));

            temp.clear();
        }

        if (data.length > maxFileSize) {

            float proportion = 1.0f * maxFileSize / data.length;
            int width = (int) (result.get().getWidth() * proportion);
            int height = (int) (result.get().getHeight() * proportion);
            SoftReference<Bitmap> scaled = new SoftReference<>(Bitmap.createScaledBitmap(result.get(), width, height, false));

            result.clear();
            return scaled.get();
        }

        return result.get();
    }


    /**
     * Drawable转Bitmap
     */
    public static Bitmap drawableToBmp(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * 旋转Bitmap图片
     *
     * @param context
     * @param degree 旋转的角度
     * @param srcBitmap 需要旋转的图片的Bitmap
     * @return
     */
    private Bitmap rotateBimap(Context context, float degree, Bitmap srcBitmap) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()
                , matrix, true);
        return bitmap;
    }
}
