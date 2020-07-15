package com.xbribe.ui.main.drawers.notification;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.ui.function.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment
{


    AppDataManager appDataManager;

    @BindView(R.id.recycler_notify)
    RecyclerView recyclerView;
    DatabaseHelperNotice databaseHelperNotice;
    SQLiteDatabase sqLiteDatabase;


    NotificationAdapter notificationAdapter;

    RecyclerView.LayoutManager layoutManager;

    List<NotificationModel> nlist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent=inflater.inflate(R.layout.fragment_notification,container,false);
        ButterKnife.bind(this,parent);
        databaseHelperNotice=new DatabaseHelperNotice(getContext());
        databaseHelperNotice.getWritableDatabase();
        initreycycleradapter();
        return parent;

    }
    private void initreycycleradapter()
    {

            notificationAdapter = new NotificationAdapter(uploadlist(),getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(notificationAdapter);
        }

    private List<NotificationModel> uploadlist()
    {
        nlist=new ArrayList<>();
        boolean ifinserted= databaseHelperNotice.insertData(appDataManager.getToken(),"nandi200@gmail.com","I am going to die","Alvalida  dosto");
        if(ifinserted==true)
        {
            Toast.makeText(getActivity(),"Data inserted",Toast.LENGTH_SHORT).show();

        }
        nlist.add(new NotificationModel("organisation name","organisation ministry","organisation department","Case Id","User Id","Some notification msg about the case being reported "));
        nlist.add(new NotificationModel("organisation name","organisation ministry","organisation department","Case Id","User Id","Some notification msg about the case being reported "));
        nlist.add(new NotificationModel("organisation name","organisation ministry","organisation department","Case Id","User Id","Some notification msg about the case being reported "));
        nlist.add(new NotificationModel("organisation name","organisation ministry","organisation department","Case Id","User Id","Some notification msg about the case being reported "));
     return  nlist;

    }
}

