package com.xbribe.ui.function;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.xbribe.Constants;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.Organizations;
import com.xbribe.service.AddressService;
import com.xbribe.ui.MyApplication;

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

public class Step_one_Fragment extends Fragment {
    private SubmissionActivityViewModel submissionActivityViewModel;
    private ArrayList<Organizations> organizationsData;
    private ArrayList<String> departmentData;
    private SpinnerAdapter1 spinnerAdapter1;
    private SpinnerAdapter2 spinnerAdapter2;

    private Step_two_Fragment step_two_fragment;

    private AppDataManager appDataManager;

    private FragmentManager fragmentManager;

    String name_oraganisation,city,pincode,description;
    String department;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;


    @BindView(R.id.btn_proceed)
    Button proceed;

    @BindView((R.id.tv_address))
    TextView tvAddress;

    @BindView(R.id.et_name_organization)
    EditText etName;

    @BindView(R.id.et_city)
    EditText etCity;

    @BindView(R.id.et_pincode)
    EditText etpincode;

    @BindView(R.id.et_desc)
    EditText etDescription;

    @BindView(R.id.spinner_ministry)
    Spinner spinnerMinistry;

    @BindView(R.id.spinner_department)
    Spinner spinnerDepartment;

    Organizations organizations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_step_one, container, false);
        ButterKnife.bind(this, parent);
        submissionActivityViewModel = ViewModelProviders.of(getActivity()).get(SubmissionActivityViewModel.class);
        submissionActivityViewModel.getOrganizationsDetails();
        appDataManager = ((MyApplication)getActivity().getApplication()).getDataManager();

        tvAddress.setText(appDataManager.getAddress());
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
                         appDataManager.saveOrgID(organizations.getID());
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
                        appDataManager.saveDepartment(department);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please reopen the screen.", Toast.LENGTH_LONG).show();
            }
        });
    }

     @OnClick(R.id.btn_proceed)
     public void senddetails()
     {
         name_oraganisation = etName.getText().toString();
         city = etCity.getText().toString();
         pincode = etpincode.getText().toString();
         description = etDescription.getText().toString();
         if(name_oraganisation.isEmpty()==true || city.isEmpty()==true ||  pincode.isEmpty()==true  ||  description.isEmpty()==true)
         {
              String msg="Please fill in the details";
              showSnackbar(msg);

         }
         else
         {
             Bundle bundle = new Bundle();
             step_two_fragment = new Step_two_Fragment();

             bundle.putString("MINISTRYID",appDataManager.getOrgID());
             bundle.putString("DEPARTMENT",appDataManager.getDepartment());
             bundle.putString("ORGANISATION",name_oraganisation);
             bundle.putString("CITY",city);
             bundle.putString("PINCODE",pincode);
             bundle.putString("DESCRIPTION",description);
             step_two_fragment.setArguments(bundle);
             getFragmentManager().beginTransaction().replace(R.id.main_frame_two,step_two_fragment).commit();

         }

     }
    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(relativeLayout,msg,Snackbar.LENGTH_INDEFINITE)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Snackbar snackbar1=Snackbar.make(relativeLayout,"Undo Successful",Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                });
        snackbar.show();
    }

}
