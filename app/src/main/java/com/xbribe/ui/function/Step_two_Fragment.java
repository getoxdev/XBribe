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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
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

    private  String name,city,pincode,ministryId,department,description;

    private ArrayList<Uri> imageList = new ArrayList<Uri>();
    private ArrayList<Uri> audioList = new ArrayList<Uri>();
    private ArrayList<Uri> videoList = new ArrayList<Uri>();

    private ArrayList<String> imageURL = new ArrayList<String>();
    private ArrayList<String> audioURL = new ArrayList<String>();
    private ArrayList<String> videoURL = new ArrayList<String>();

    private StorageReference mStorageRef;
    private AppDataManager appDataManager;
    private SecretFragment secretFragment;
    private OTPVerifyFragment otpVerifyFragment;

    private DatabaseHelper databaseHelper;

    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.rview_selected)
    RecyclerView recyclerView;

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

        return parent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mStorageRef = FirebaseStorage.getInstance().getReference();
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
        Intent intent1 = new Intent(getActivity(), FilePickerActivity.class);
        intent1.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowVideos(false)
                .setShowAudios(false)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .enableImageCapture(true)
                .build());
        startActivityForResult(intent1, PICK_IMAGE_REQUEST);
    }

    private void openAudioChoose()
    {
        Intent intent2 = new Intent(getActivity(), FilePickerActivity.class);
        intent2.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setShowAudios(true)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());
        startActivityForResult(intent2, PICK_AUDIO_REQUEST);
    }

    private void openVideoChoose()
    {
        Intent intent3 = new Intent(getActivity(), FilePickerActivity.class);
        intent3.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowVideos(true)
                .setShowAudios(false)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .enableVideoCapture(true)
                .build());
        startActivityForResult(intent3, PICK_VIDEO_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK)
        {
            ArrayList<MediaFile> imgList = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            int i=0;
            while(i<imgList.size())
            {
                Uri image = imgList.get(i).getUri();
                imageList.add(image);
                i++;
            }
        }

        if(requestCode==PICK_AUDIO_REQUEST && resultCode==RESULT_OK)
        {
            ArrayList<MediaFile> audList = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            int j=0;
            while(j<audList.size())
            {
                Uri audio = audList.get(j).getUri();
                audioList.add(audio);
                j++;
            }
        }

        if(requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK)
        {
            ArrayList<MediaFile> vidList = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            int k=0;
            while(k<vidList.size())
            {
                Uri video = vidList.get(k).getUri();
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
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
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
                                    hideProgress();
                                    String msg="Uploaded Image Successfully";
                                    showSnackbar(msg);
                                    Uri downloadUri = task.getResult();
                                    String imgURL = downloadUri.toString();
                                    imageURL.add(imgURL);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        String msg="Not  Uploaded";
                        showSnackbar(msg);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        showProgress();
                    }
                });
            }
            else
            {
                String msg="No File Selected";
                showSnackbar(msg);
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
                                hideProgress();
                                if (task.isSuccessful()) {
                                    hideProgress();
                                    String msg="Uploaded Successfully";
                                    showSnackbar(msg);
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
                        String msg="Not Uploaded";
                        showSnackbar(msg);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        showProgress();
                    }
                });
            }
            else
            {
                String msg="No File Selected";
                showSnackbar(msg);
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
                        String msg="Uploaded Successfully";
                        showSnackbar(msg);

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
                        String msg="Not uploaded";
                        showSnackbar(msg);
                    }
                });
            }
            else
            {
                String msg="No File Selected";
                showSnackbar(msg);
            }
        }
        videoList.clear();
    }

    @OnClick(R.id.btn_upload)
    void upload()
    {
        uploadImage();
        uploadAudio();
        uploadVideo();
    }

    @OnClick(R.id.btn_submit)
    void submit()
    {
        Bundle bundle = new Bundle();
        bundle.putString("MINISTRYID",ministryId);
        bundle.putString("DEPARTMENT",department);
        bundle.putString("ORGANISATION",name);
        bundle.putString("CITY",city);
        bundle.putString("PINCODE",pincode);
        bundle.putString("DESCRIPTION",description);
        bundle.putStringArrayList("IMGARRAY",imageURL);
        bundle.putStringArrayList("AUDARRAY",audioURL);
        bundle.putStringArrayList("VIDARRAY",videoURL);

        otpVerifyFragment = new OTPVerifyFragment();
        otpVerifyFragment.setArguments(bundle);
        submissionActivityViewModel.setSendOtp();
        submissionActivityViewModel.getSendOtp().observe(this, data->{
            if(data!=null)
            {
                Toast.makeText(getActivity(),"Please check your email.",Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_two,otpVerifyFragment)
                        .commit();
            }
            else
            {
                String msg = "Please try later!";
                showSnackbar(msg);
            }
        });
    }

    @OnClick(R.id.secret_camera)
    void openSecretCamera()
    {
        secretFragment = new SecretFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_two,secretFragment)
                .addToBackStack("Step 2")
                .commit();
    }

    @Override
    public void onDestroy()
    {
        imageURL.clear();
        audioURL.clear();
        videoURL.clear();
        super.onDestroy();
    }

    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(constraintLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showProgress() {
        pbProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbProgress.setVisibility(View.GONE);
    }
}
