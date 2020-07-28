package com.xbribe.ui.function;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.xbribe.Constants;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.Organizations;
import com.xbribe.service.AddressService;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.MainActivity;
import com.xbribe.ui.main.ReportFragment;
import com.xbribe.ui.main.drawers.drafts.DatabaseSaveDraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Step_one_Fragment extends Fragment
{

    private SubmissionActivityViewModel submissionActivityViewModel;
    private ArrayList<Organizations> organizationsData;
    private ArrayList<String> departmentData;
    private SpinnerAdapter1 spinnerAdapter1;
    private SpinnerAdapter2 spinnerAdapter2;
    private Step_two_Fragment step2Fragment;
    private ReportFragment reportFragment;
    private AppDataManager appDataManager;

    public String name_oraganisation,city,pincode,description,department,ministryId,official;

    private Organizations organizations;

    DatabaseSaveDraft databaseSaveDraft;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @BindView(R.id.btn_savedrf)
    Button savedraft;

    @BindView(R.id.btn_proceed)
    Button proceed;

    @BindView((R.id.tv_address))
    TextView tvAddress;

    @BindView(R.id.et_name_organization)
    EditText etName;

    @BindView(R.id.et_city)
    EditText etCity;

    @BindView(R.id.et_pincode)
    EditText etPincode;

    @BindView(R.id.et_desc)
    EditText etDescription;

    @BindView(R.id.et_official_name)
    TextInputEditText etOfficialName;

    @BindView(R.id.spinner_ministry)
    Spinner spinnerMinistry;

    @BindView(R.id.spinner_department)
    Spinner spinnerDepartment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_step_one, container, false);
        ButterKnife.bind(this, parent);
        databaseSaveDraft=new DatabaseSaveDraft(getActivity());
        databaseSaveDraft.getWritableDatabase();

        step2Fragment = new Step_two_Fragment();
        reportFragment = new ReportFragment();
        Bundle bundleDraft = getActivity().getIntent().getExtras();
        if(bundleDraft!=null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("MINISTRYID",bundleDraft.getString("MINISTRYID"));
            bundle.putString("DEPARTMENT",bundleDraft.getString("DEPARTMENT"));
            bundle.putString("ORGANISATION",bundleDraft.getString("ORGANISATION"));
            bundle.putString("OFFICIAL",bundleDraft.getString("OFFICIAL"));
            bundle.putString("CITY",bundleDraft.getString("CITY"));
            bundle.putString("PINCODE",bundleDraft.getString("PINCODE"));
            bundle.putString("DESCRIPTION",bundleDraft.getString("DESCRIPTION"));
            bundle.putString("ADDRESS",bundleDraft.getString("ADDRESS"));
            bundle.putString("LATITUDE",bundleDraft.getString("LONGITUDE"));
            bundle.putString("LONGITUDE",bundleDraft.getString("LONGITUDE"));
            step2Fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_two,step2Fragment)
                    .commit();
        }
        submissionActivityViewModel = ViewModelProviders.of(getActivity()).get(SubmissionActivityViewModel.class);
        submissionActivityViewModel.getOrganizationsDetails();
        appDataManager = ((MyApplication)getActivity().getApplication()).getDataManager();
        tvAddress.setText(appDataManager.getAddress().trim());

        etCity.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        etName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        etPincode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        etDescription.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();

        submissionActivityViewModel.getOrganizationsResponse().observe(this, data -> {
            if (data != null)
            {
                organizationsData = new ArrayList<>(Arrays.asList(data.getData()));
                spinnerAdapter1 = new SpinnerAdapter1(getActivity(), organizationsData);
                spinnerMinistry.setAdapter(spinnerAdapter1);
                spinnerMinistry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         organizations = (Organizations)parent.getSelectedItem();
                         appDataManager.saveMinistry(organizations.getMinistry());
                         ministryId=organizations.getID();
                         appDataManager.saveOrgID(ministryId);
                         departmentData = new ArrayList<>(Arrays.asList(organizations.getDepartments()));
                         spinnerAdapter2 = new SpinnerAdapter2(getActivity(),departmentData);
                         spinnerDepartment.setAdapter(spinnerAdapter2);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        department = (String)parent.getSelectedItem();
                        if(department!=null)
                        {
                            appDataManager.saveDepartment(department);
                        }
                        else
                        {
                            department="No Department present of the Ministry";
                            appDataManager.saveDepartment("No Department of the Ministry");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

     @OnClick(R.id.btn_proceed)
     public void sendDetails()
     {
         name_oraganisation = etName.getText().toString();
         city = etCity.getText().toString();
         pincode = etPincode.getText().toString();
         description = etDescription.getText().toString();
         official = etOfficialName.getText().toString();

         if(name_oraganisation.isEmpty()==true || city.isEmpty()==true ||
                 pincode.isEmpty()==true  ||  description.isEmpty()==true)
         {
              String msg="Please fill in the details";
              showSnackbar(msg);
         }
         else
         {
             Bundle bundle = new Bundle();
             bundle.putString("MINISTRYID",ministryId);
             bundle.putString("DEPARTMENT",department);
             bundle.putString("ORGANISATION",name_oraganisation);
             if(official.isEmpty())
                 bundle.putString("OFFICIAL","Not Reported");
             else
                 bundle.putString("OFFICIAL",official);
             bundle.putString("CITY",city);
             bundle.putString("PINCODE",pincode);
             bundle.putString("DESCRIPTION",description);
             bundle.putString("ADDRESS",appDataManager.getAddress());
             bundle.putString("LATITUDE",appDataManager.getLatitude());
             bundle.putString("LONGITUDE",appDataManager.getLongitude());
             step2Fragment.setArguments(bundle);
             getActivity().getSupportFragmentManager().beginTransaction()
                     .replace(R.id.main_frame_two,step2Fragment)
                     .addToBackStack("Step 1")
                     .commit();
         }
     }
    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(relativeLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    @OnClick(R.id.btn_savedrf)
    void saveDraft()
    {
        name_oraganisation = etName.getText().toString();
        city = etCity.getText().toString();
        pincode = etPincode.getText().toString();
        description = etDescription.getText().toString();
        official=etOfficialName.getText().toString();
        if(name_oraganisation.isEmpty()==true || city.isEmpty()==true ||
                pincode.isEmpty()==true  ||  description.isEmpty()==true)
        {
            String msg="Please fill in the details";
            showSnackbar(msg);
        }
        else
        {
            if(official.isEmpty())
            {
                boolean ifInserted= databaseSaveDraft.insertData(appDataManager.getMinistry(),appDataManager.getAddress(),pincode,city,department,name_oraganisation,description,appDataManager.getEmail(),appDataManager.getLatitude(),appDataManager.getLongitude(),"Not Reported",appDataManager.getOrgID());
                if(ifInserted==true)
                {
                    String msg="Draft Saved";
                    showSnackbar(msg);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else
                {
                    String msg="Not saved. Please try again!";
                    showSnackbar(msg);
                }
            }
            else
            {
                boolean ifInserted= databaseSaveDraft.insertData(appDataManager.getMinistry(),appDataManager.getAddress(),pincode,city,department,name_oraganisation,description,appDataManager.getEmail(),appDataManager.getLatitude(),appDataManager.getLongitude(),official,appDataManager.getOrgID());
                if(ifInserted==true)
                {
                    String msg="Draft Saved";
                    showSnackbar(msg);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else
                {
                    String msg="Not saved. Please try again!";
                    showSnackbar(msg);
                }
            }
        }
    }

}
