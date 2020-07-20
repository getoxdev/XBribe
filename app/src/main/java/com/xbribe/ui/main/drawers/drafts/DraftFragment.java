package com.xbribe.ui.main.drawers.drafts;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import com.xbribe.ui.function.Step_two_Fragment;
import com.xbribe.ui.function.SubmissionActivity;
import com.xbribe.ui.main.drawers.checkcase.CheckcaseModel;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftFragment extends Fragment
{
    @BindView(R.id.recycler_checkdrafts)
    RecyclerView recyclerView;

    FragmentManager fragmentManager;



    @BindView(R.id.tv_no_cases)
    TextView nocases;


    Cursor  cursor;

    List<DraftModel> draftModelList;


    DraftAdapter draftAdapter;

   DatabaseSaveDraft databaseSaveDraft;


    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_drafts,container,false);
        ButterKnife.bind(this,parent);
        databaseSaveDraft=new DatabaseSaveDraft(getActivity());
        databaseSaveDraft.getWritableDatabase();
        initrecycleradapter();
        return parent;

    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
           // Log.d("Position", "onClick: POsition ::: " + position);
            goToSteptwo(position);
        }
    };

    private void goToSteptwo(int position)
    {
        Step_two_Fragment step_two_fragment=new Step_two_Fragment();
        Bundle bundle=new Bundle();
        cursor = databaseSaveDraft.getrowdetails(position);
        //Log.i(TAG, "Position"+position);
        if (cursor.moveToFirst())
        {
            do {
                bundle.putString("MINISTRYID", cursor.getString(1));
                bundle.putString("DEPARTMENT", cursor.getString(5));
                bundle.putString("ORGANISATION", cursor.getString(6));
                bundle.putString("CITY", cursor.getString(4));
                bundle.putString("PINCODE", cursor.getString(3));
                bundle.putString("DESCRIPTION", cursor.getString(7));
            }
            while (cursor.moveToNext());
        }
        step_two_fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame,step_two_fragment)
                .addToBackStack("Step 1")
                .commit();
    }

    private void initrecycleradapter()
    {
        cursor=databaseSaveDraft.getAllDetails();
        if(cursor.getCount()==0)
        {
            nocases.setVisibility(View.VISIBLE);
            showMessage("Error","Nothing found");

        }

        draftAdapter =new DraftAdapter(uploadlist(),getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(draftAdapter);
        draftAdapter.setOnItemClickListener(onClickListener);

    }
    private  List<DraftModel> uploadlist()
    {
        draftModelList=new ArrayList<>();
        int i=0;
        while (cursor.moveToNext())
        {
            draftModelList.add(new DraftModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7)));
            i++;
        }
        return draftModelList;
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

