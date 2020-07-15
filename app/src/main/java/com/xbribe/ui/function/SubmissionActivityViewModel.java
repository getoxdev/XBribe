package com.xbribe.ui.function;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.CaseData;
import com.xbribe.data.models.CaseResponse;
import com.xbribe.data.models.LocationDetails;
import com.xbribe.data.models.OrganizationResponse;
import com.xbribe.ui.MyApplication;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmissionActivityViewModel extends AndroidViewModel {

    private AppDataManager appDataManager;
    private MutableLiveData<OrganizationResponse> organizationResponse;
    private MutableLiveData<CaseData> caseResponse;


    public SubmissionActivityViewModel(@NonNull Application application)
    {
        super(application);
        appDataManager = ((MyApplication)application).getDataManager();
        organizationResponse = new MutableLiveData<>();
        caseResponse = new MutableLiveData<>();
    }

    public MutableLiveData<OrganizationResponse> getOrganizationsResponse() { return organizationResponse; }

    public  MutableLiveData<CaseData> getCaseResponse()
    {
        return caseResponse;
    }

  public  void reportCaseDetails(String token,String ministryId, String department, String name, String place, String address, String pin, String latitude, String longitude, String description, ArrayList<String> picsArray, ArrayList<String> audiosArray, ArrayList<String> videosArray)
  {
      appDataManager.reportCase(token,ministryId,department,name,place,address,pin,latitude,longitude,description,picsArray,audiosArray,videosArray).enqueue(new Callback<CaseData>() {
          @Override
          public void onResponse(Call<CaseData> call, Response<CaseData> response)
          {
              if (response.code() < 300) {
                  caseResponse.postValue(response.body());
              } else
                  {
                   caseResponse.postValue(null);
              }
          }

          @Override
          public void onFailure(Call<CaseData> call, Throwable t) {
               caseResponse.postValue(null);
          }
      });
  }


    public void getOrganizationsDetails()
    {
        appDataManager.getOrganizations().enqueue(new Callback<OrganizationResponse>()
        {
            @Override
            public void onResponse(Call<OrganizationResponse> call, Response<OrganizationResponse> response) {
                if(response.code()<300)
                {
                    organizationResponse.postValue(response.body());

                }
                else
                {
                    organizationResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<OrganizationResponse> call, Throwable t) {
                organizationResponse.postValue(null);
            }
        });
    }
}
