package com.xbribe.ui.function;

import android.content.Context;
import android.graphics.Camera;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class CameraManager extends Camera {

    public static CameraManager getInstance(Context context) {
        if(mManager == null) mManager = new CameraManager(context);
        return mManager;
    }

    private CameraManager(Context context) {
        mContext = context;
    }

    private static CameraManager mManager;

    private Context mContext;

    @Override
    public void onError(int error, Camera camera) {
        switch (error) {
            case Camera.CAMERA_ERROR_SERVER_DIED:
                Log.e(TAG, "Camera error: Media server died");
                break;
            case Camera.CAMERA_ERROR_UNKNOWN:
                Log.e(TAG, "Camera error: Unknown");
                break;
            case Camera.CAMERA_ERROR_EVICTED:
                Log.e(TAG, "Camera error: Camera was disconnected due to use by higher priority user");
                break;
            default:
                Log.e(TAG, "Camera error: no such error id (" + error + ")");
                break;
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        try {
            if(autoFocusSupported(camera)) {
                mCamera.autoFocus(this);
            } else {
                camera.setPreviewCallback(null);
                camera.takePicture(null, null, this);
            }
        } catch (Exception e) {
            Log.e(TAG, "Camera error while taking picture");
            e.printStackTrace();
            releaseCamera();
        }
    }

    private void initCamera() {
        new AsyncTask() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Cannot open camera");
                    e.printStackTrace();
                    isWorking = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    if(mCamera != null) {
                        mSurface = new SurfaceTexture(123);
                        mCamera.setPreviewTexture(mSurface);

                        Camera.Parameters params = mCamera.getParameters();
                        int angle = 270;//getCameraRotationAngle(Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
                        params.setRotation(angle);

                        if (autoFocusSupported(mCamera)) {
                            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                        } else {
                            Log.w(TAG, "Autofocus is not supported");
                        }

                        mCamera.setParameters(params);
                        mCamera.setPreviewCallback(CameraManager.this);
                        mCamera.setErrorCallback(CameraManager.this);
                        mCamera.startPreview();
                        muteSound();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Cannot set preview for the front camera");
                    e.printStackTrace();
                    releaseCamera();
                }
            }

        }.execute();
    }

    private void releaseCamera() {
        if(mCamera != null) {
            mCamera.release();
            mSurface.release();
            mCamera = null;
            mSurface = null;
        }
        unmuteSound();
    }

    private void unmuteSound() {
        if(mContext != null) {
            AudioManager mgr = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mgr.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
            } else {
                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            }
        }
    }

    private void muteSound() {
        if(mContext != null) {
            AudioManager mgr = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mgr.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
            } else {
                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
        }
    }

    public int getCameraRotationAngle(int cameraId, android.hardware.Camera camera) {
        int result = 270;
        if(camera != null && mContext != null) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            int degrees = getRotationAngle(rotation);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360; //compensates mirroring
            }
        }
        return result;
    }

    private int getRotationAngle(int rotation) {
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    private boolean autoFocusSupported(Camera camera) {
        if(camera != null) {
            Camera.Parameters params = camera.getParameters();
            List focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                return true;
            }
        }
        return false;
    }

}