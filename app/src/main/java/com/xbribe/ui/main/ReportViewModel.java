package com.xbribe.ui.main;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.TokenResponse;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends AndroidViewModel {

    private AppDataManager appDataManager;
    private MutableLiveData<Boolean> emailResponse;

    public ReportViewModel(@NonNull Application application) {
        super(application);

        appDataManager = ((MyApplication) application).getDataManager();
        emailResponse = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getEmailResponse() {
        return emailResponse;
    }

    public void getEmailDetails() {
        appDataManager.getDetails(appDataManager.getToken()).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.code() < 300) {
                    emailResponse.postValue(true);
                    appDataManager.saveEmail(response.body().getEmail());
                    appDataManager.saveID(response.body().getID());
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                emailResponse.postValue(false);
            }
        });
    }
}

