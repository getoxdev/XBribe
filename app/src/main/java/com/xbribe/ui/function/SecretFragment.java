package com.xbribe.ui.function;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraFragment;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.google.android.material.button.MaterialButton;
import com.xbribe.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecretFragment extends Fragment {
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    @BindView(R.id.btn_record)
    MaterialButton record;

    @BindView(R.id.btn_stop)
    MaterialButton stop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_secret,container,false);
        ButterKnife.bind(this,parent);

        String timeStamp = new SimpleDateFormat("MMdd_HHmm").format(new Date());

        stop.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xbribe"+timeStamp+".amr";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        return parent;
    }

    @OnClick(R.id.btn_record)
    void startRecord()
    {
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException ise) {
            Log.e("Record Error",ise.getMessage());
        } catch (IOException ioe) {
            Log.e("Record Error",ioe.getMessage());
        }
        record.setEnabled(false);
        stop.setEnabled(true);
        Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_stop)
    void stopRecord()
    {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = new MediaRecorder();
        record.setEnabled(true);
        stop.setEnabled(false);
        Toast.makeText(getActivity(), "Audio Recorded successfully", Toast.LENGTH_LONG).show();
    }
}
