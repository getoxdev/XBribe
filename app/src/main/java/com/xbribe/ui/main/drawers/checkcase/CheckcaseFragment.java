package com.xbribe.ui.main.drawers.checkcase;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.CollecImages;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.function.DatabaseHelper;
import com.xbribe.ui.function.SubmissionActivityViewModel;
import com.xbribe.ui.main.drawers.checkcase.checkcasedesc.CheckCaseDescFragment;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckcaseFragment extends  Fragment
{



    ArrayList<CollecImages> collecImages=new ArrayList<>();

    ArrayList<String>  imagelist=new ArrayList();

    AppDataManager appDataManager;

    Cursor cursor;

    DatabaseHelper databaseHelper;

    @BindView(R.id.tv_no_cases)
    TextView nocases;

    @BindView(R.id.recycler_checkcase)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    CheckCaseAdapter checkCaseAdapter;

    List<CheckcaseModel> caselist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_check_case, container, false);
        ButterKnife.bind(this, parent);
        databaseHelper=new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://5ee7467352bb0500161fd73a.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImageApiService imageApiService=retrofit.create(ImageApiService.class);
        Call<ArrayList<CollecImages>> call=imageApiService.getImages();
        call.enqueue(new Callback<ArrayList<CollecImages>>() {
            @Override
            public void onResponse(Call<ArrayList<CollecImages>> call, Response<ArrayList<CollecImages>> response)
            {

                if(response.code()==200)
                {
                    collecImages = response.body();
                }
                int i=0;
                for (CollecImages coll : collecImages)
                {
                    imagelist.add(i,coll.getImageUrl());
                    i++;
                }
                initreycycleradapter(imagelist);
            }
            @Override
            public void onFailure(Call<ArrayList<CollecImages>> call, Throwable t)
            {
                Toast.makeText(getActivity(),"Error"+t.toString(),Toast.LENGTH_LONG).show();
            }
        });

        return parent;

    }

    public void initreycycleradapter(ArrayList<String> imagelist)
    {
        cursor=databaseHelper.getAllDetails();
        if(cursor.getCount()==0)
        {
            nocases.setVisibility(View.VISIBLE);
            showMessage("Error","Nothing found");

        }
        else {
            checkCaseAdapter = new CheckCaseAdapter(getContext(), uploadlist(imagelist));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(checkCaseAdapter);
        }
    }


    public List<CheckcaseModel> uploadlist(ArrayList<String> imag)
    {
        caselist = new ArrayList<>();
            int i=0;
            while (cursor.moveToNext())
            {
             caselist.add(new CheckcaseModel(imag.get(i), cursor.getString(5), cursor.getString(4), cursor.getString(10), "GET DETAILS"));
             i++;
            }

            return caselist;

    }
    private void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}

