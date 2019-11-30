package com.example.lndonesiablend.load;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by SiKang on 2018/12/14.
 */
public class CameraLoadView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private final String TAG = "CameraView";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    //默认预览尺寸
    private int imageWidth = 1920;
    private int imageHeight = 1080;
    //默认View尺寸
    private int defaultWidth = 0;
    private int defaultHeight = 0;

    private boolean isCaptureImage = false;


    private OnSurfaceChangedListener surfaceChangedListener;

    public CameraLoadView(Context context) {
        super(context);
        init();
    }

    public CameraLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        //设置SurfaceView 的SurfaceHolder的回调函数
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Surface创建时开启Camera
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            release();
        } catch (Exception e) {
        }
    }


    private PreviewPictureCallback previewPictureCallback;

    public void captureImage(Camera.PictureCallback pictureCallback) {
        mCamera.takePicture(null, null, pictureCallback);
    }

    public void captureImage(PreviewPictureCallback pictureCallback) {
        this.previewPictureCallback = pictureCallback;
        isCaptureImage = true;
    }

    public interface PreviewPictureCallback {
        void onImage(Bitmap bitmap);
    }

    public void setTargetPreviewSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public interface OnSurfaceChangedListener {
        void onSurfaceChanged(int finalWidth, int finalHeight);
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (previewPictureCallback != null && isCaptureImage) {
            isCaptureImage = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Camera.Size size = camera.getParameters().getPreviewSize();
                    final YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    if (bmp != null) {
                        previewPictureCallback.onImage(bmp);
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        defaultWidth = MeasureSpec.getSize(widthMeasureSpec);
        defaultHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 摄像头配置
     */
    public void initCameraParams() {
        stopPreview();
        Camera.Parameters camParams = mCamera.getParameters();
        List<Camera.Size> sizes = camParams.getSupportedPreviewSizes();
        for (int i = 0; i < sizes.size(); i++) {
            if ((sizes.get(i).width >= imageWidth && sizes.get(i).height >= imageHeight) || i == sizes.size() - 1) {
                imageWidth = sizes.get(i).width;
                imageHeight = sizes.get(i).height;
//                Log.v(TAG, "Changed to supported resolution: " + imageWidth + "x" + imageHeight);
                break;
            }
        }
//        camParams.setPreviewSize(imageWidth, imageHeight);
//        camParams.setPictureSize(imageWidth, imageHeight);
//        Log.v(TAG, "Setting imageWidth: " + imageWidth + " imageHeight: " + imageHeight + " frameRate: " + frameRate);
//        Log.v(TAG, "Preview Framerate: " + camParams.getPreviewFrameRate());
//        mCamera.setParameters(camParams);
        //取到的图像默认是横向的，这里旋转90度，保持和预览画面相同
        mCamera.setDisplayOrientation(90);
        if (defaultWidth != 0) {
            float xy = ((float) imageWidth) / imageHeight;
            int heightSize = (int) (xy * defaultWidth);
            layout(0, (defaultHeight - heightSize) / 2, defaultWidth, heightSize + (defaultHeight - heightSize) / 2);
            startPreview();
        }

    }

    /**
     * 开始预览
     */
    public void startPreview() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(this);
                mCamera.setPreviewDisplay(mHolder);//set the surface to be used for live preview
                mCamera.startPreview();
                mCamera.autoFocus(autoFocusCB);
            }
        } catch (Exception e) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        }
    }

    /**
     * 打开指定摄像头
     */
    public void openCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    mCamera = Camera.open(cameraId);
                    if (mCamera != null) {
                        initCameraParams();
                    }
                } catch (Exception e) {
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                }
                break;
            }
        }
    }

    /**
     * 摄像头自动聚焦
     */
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            postDelayed(doAutoFocus, 2000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null) {
                try {
                    mCamera.autoFocus(autoFocusCB);
                } catch (Exception e) {
                }
            }
        }
    };

    /**
     * 释放
     */
    public void release() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

}

