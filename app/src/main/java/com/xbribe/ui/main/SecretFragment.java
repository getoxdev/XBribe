package com.xbribe.ui.main;

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
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.xbribe.R;

import java.io.File;
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
        return parent;
    }

    @OnClick(R.id.btn_record)
    void startRecord()
    {
        imgOn.setVisibility(View.VISIBLE);
        imgOff.setVisibility(View.INVISIBLE);
        startRecording();
        Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_stop)
    void stopRecord()
    {
        imgOn.setVisibility(View.INVISIBLE);
        imgOff.setVisibility(View.VISIBLE);
        stopRecording();
        Toast.makeText(getActivity(), "Audio Recorded successfully", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_open_scamera)
    void openSecretCamera()
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame,secretCameraFragment)
                    .addToBackStack("Secret Fragment")
                    .commit();
    }

    private void startRecording()
    {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        String timeStamp = new SimpleDateFormat("MMdd_HHmm").format(new Date());
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "XBribe" + File.separator + "aud"+timeStamp+".mp3";
        myAudioRecorder.setOutputFile(outputFile);

        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            Log.e("Audio Recorder",e.getMessage());
        }

        myAudioRecorder.start();
    }

    private void stopRecording()
    {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder=null;
    }
}
