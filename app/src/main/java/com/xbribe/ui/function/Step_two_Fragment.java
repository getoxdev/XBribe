package com.xbribe.ui.function;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.CaseData;
import com.xbribe.data.models.LocationDetails;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class Step_two_Fragment  extends Fragment
{
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;
    private static final int PICK_VIDEO_REQUEST = 3;

    private int imageCount,audioCount,videoCount;
    private  String name,city,pincode,ministryId,department,description;

    private ArrayList<Uri> imageList = new ArrayList<Uri>();
    private ArrayList<Uri> audioList = new ArrayList<Uri>();
    private ArrayList<Uri> videoList = new ArrayList<Uri>();

    private ArrayList<String> imageURL = new ArrayList<String>();
    private ArrayList<String> audioURL = new ArrayList<String>();
    private ArrayList<String> videoURL = new ArrayList<String>();

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private AppDataManager appDataManager;

    DatabaseHelper databaseHelper;
    SQLiteDatabase database;

    @BindView(R.id.imag_camera)
    ImageButton imgChoose;

    @BindView(R.id.imag_audio)
    ImageButton audChoose;

    @BindView(R.id.imag_video)
    ImageButton vidChoose;

    @BindView(R.id.btn_upload)
    Button upload;

    @BindView(R.id.btn_submit)
    Button submit;

    @BindView(R.id.secret_camera)
    FloatingActionButton btnSecretCamera;

    SubmissionActivityViewModel submissionActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View parent=inflater.inflate(R.layout.fragment_step_two,container,false);
        ButterKnife.bind(this,parent);
        submissionActivityViewModel=ViewModelProviders.of(getActivity()).get(SubmissionActivityViewModel.class);
        appDataManager = ((MyApplication) getActivity().getApplication()).getDataManager();

        //Code for calling the Receiver class
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        imgChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openImageChoose();
            }
        });

        audChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openAudioChoose();
            }
        });

        vidChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoChoose();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                uploadAudio();
                uploadVideo();
            }
        });

        return parent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        databaseHelper=new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();
        ministryId=getArguments().getString("MINISTRYID");
        department=getArguments().getString("DEPARTMENT");
        name=getArguments().getString("ORGANISATION");
        city=getArguments().getString("CITY");
        pincode=getArguments().getString("PINCODE");
        description=getArguments().getString("DESCRIPTION");

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openImageChoose()
    {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent1,PICK_IMAGE_REQUEST);
    }

    private void openAudioChoose()
    {
        Intent intent2 = new Intent();
        intent2.setType("audio/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent2,PICK_AUDIO_REQUEST);
    }

    private void openVideoChoose()
    {
        Intent intent3 = new Intent();
        intent3.setType("video/*");
        intent3.setAction(Intent.ACTION_GET_CONTENT);
        intent3.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent3,PICK_VIDEO_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getClipData()!=null)
        {
             imageCount = data.getClipData().getItemCount();
            int i=0;
            while(i<imageCount)
            {
                Uri image = data.getClipData().getItemAt(i).getUri();
                imageList.add(image);
                i++;
            }
        }

        if(requestCode==PICK_AUDIO_REQUEST && resultCode==RESULT_OK && data!=null && data.getClipData()!=null)
        {
             audioCount = data.getClipData().getItemCount();
            int j=0;
            while(j<audioCount)
            {
                Uri audio = data.getClipData().getItemAt(j).getUri();
                audioList.add(audio);
                j++;
            }
        }

        if(requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK && data!=null && data.getClipData()!=null)
        {
             videoCount = data.getClipData().getItemCount();
            int k=0;
            while(k<videoCount)
            {
                Uri video = data.getClipData().getItemAt(k).getUri();
                videoList.add(video);
                k++;
            }
        }
    }
    private void uploadImage()
    {
        for(int i=0;i<imageList.size();i++)
        {
            Uri mImageUri = imageList.get(i);
            if(mImageUri!=null)
            {
                StorageReference imageReference = mStorageRef.child("images/").child(appDataManager.getID()+"/"+System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                UploadTask imageTask = imageReference.putFile(mImageUri);
                imageTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = imageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String imgURL = downloadUri.toString();
                                    imageURL.add(imgURL);
                                }
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Not uploaded!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(), "No file selected!", Toast.LENGTH_SHORT).show();
            }
        }
        imageList.clear();
    }

    private void uploadAudio()
    {
        for(int i=0;i<audioList.size();i++)
        {
            Uri mAudioUri = audioList.get(i);
            if(mAudioUri!=null)
            {
                StorageReference audioReference = mStorageRef.child("audio/").child(appDataManager.getID()+"/"+System.currentTimeMillis()+"."+getFileExtension(mAudioUri));
                UploadTask audioTask = audioReference.putFile(mAudioUri);
                audioTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                        Task<Uri> urlTask = audioTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return audioReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String audURL = downloadUri.toString();
                                    audioURL.add(audURL);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Not uploaded!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(), "No file selected!", Toast.LENGTH_SHORT).show();
            }
        }
        audioList.clear();
    }

    private void uploadVideo()
    {
        for(int i=0;i<videoList.size();i++)
        {
            Uri mVideoUri = videoList.get(i);
            if(mVideoUri!=null)
            {
                StorageReference videoReference = mStorageRef.child("video/").child(appDataManager.getID()+"/"+System.currentTimeMillis()+"."+getFileExtension(mVideoUri));
                UploadTask videoTask = videoReference.putFile(mVideoUri);
                videoTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = videoTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return videoReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String vidURL = downloadUri.toString();
                                    videoURL.add(vidURL);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Not uploaded!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(), "No file selected!", Toast.LENGTH_SHORT).show();
            }
        }
        videoList.clear();
    }

    @OnClick(R.id.btn_submit)
    void submit()
    {
        submissionActivityViewModel.reportCaseDetails(appDataManager.getToken(),ministryId,department,name,city,appDataManager.getAddress(),pincode,appDataManager.getLatitude(),appDataManager.getLongitude(),description,imageURL,audioURL,videoURL);

        submissionActivityViewModel.getCaseResponse().observe(this, data ->
        {
            if(data == null)
            {
                Toast.makeText(getActivity(), "Error! Please try again.", Toast.LENGTH_LONG).show();
            }
            else {
                String imagescount=String.valueOf(imageCount);
                String audiocount=String.valueOf(audioCount);
                String videocount=String.valueOf(videoCount);

                boolean ifInserted= databaseHelper.insertData(appDataManager.getToken(),appDataManager.getAddress(),description,appDataManager.getMinistry(),department,name,imagescount,audiocount,videocount,"CASE_PROCESS","CASE ID",appDataManager.getID());
                if(ifInserted==true)
                {
                    Toast.makeText(getActivity(),"Data inserted",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

    }

    @OnClick(R.id.secret_camera)
    void openSecretCamera()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(cameraIntent);
    }

    @Override
    public void onDestroy()
    {
        imageURL.clear();
        audioURL.clear();
        videoURL.clear();
        super.onDestroy();
    }
}
