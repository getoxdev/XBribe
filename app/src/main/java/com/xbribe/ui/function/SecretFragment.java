package com.xbribe.ui.function;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private SecretCamera secretCameraFragment;

    @BindView(R.id.btn_record)
    MaterialButton record;

    @BindView(R.id.btn_stop)
    MaterialButton stop;

    @BindView(R.id.img_off)
    ImageView imgOff;

    @BindView(R.id.img_on)
    ImageView imgOn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_secret,container,false);
        ButterKnife.bind(this,parent);

        secretCameraFragment = new SecretCamera();
        stop.setEnabled(false);
        return parent;
    }

    @OnClick(R.id.btn_record)
    void startRecord()
    {
        imgOn.setVisibility(View.VISIBLE);
        imgOff.setVisibility(View.INVISIBLE);
        try {
            String timeStamp = new SimpleDateFormat("MMdd_HHmm").format(new Date());
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audxbribe"+timeStamp+".amr";
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
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
        imgOn.setVisibility(View.INVISIBLE);
        imgOff.setVisibility(View.VISIBLE);
        try {
            myAudioRecorder.stop();
            myAudioRecorder.reset();
            myAudioRecorder.release();
        }
        catch (Exception e)
        {
            Log.e("Error",e.getMessage());
        }
        record.setEnabled(true);
        stop.setEnabled(false);
        Toast.makeText(getActivity(), "Audio Recorded successfully", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_open_scamera)
    void openSecretCamera()
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_two,secretCameraFragment)
                    .addToBackStack("Secret Fragment")
                    .commit();
    }
}
