package com.xbribe.ui.main.drawers.checkcase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xbribe.R;
import com.xbribe.data.models.CollecImages;
import com.xbribe.ui.function.DatabaseHelper;
import com.xbribe.ui.main.drawers.checkcase.checkcasedesc.CheckCaseDescFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckCaseAdapter extends RecyclerView.Adapter<CheckCaseAdapter.ViewHolder>
{


    CollecImages collecImages;
    @BindView(R.id.tv_no_cases)
    TextView no_cases;

    private  CheckCaseDescFragment checkCaseDesc;

    @BindView(R.id.btn_case_detail)
    Button casedetail;

    FragmentManager fragmentManager;
    Context context;
    List<CheckcaseModel>  checklist;

    public CheckCaseAdapter(Context context, List<CheckcaseModel> checklist) {
        this.context = context;
        this.checklist = checklist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_case,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Glide.with(context).load(checklist.get(position).getCrimeimage()).into(holder.imageView);
        holder.organization_name.setText(checklist.get(position).getName_organization());
        holder.oranization_desc.setText(checklist.get(position).getCase_desc());
        holder.casedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                setCheckCaseDesc(position);
            }
        });
    }
    public void setCheckCaseDesc(int position)
    {

        checkCaseDesc=new CheckCaseDescFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("Position passed",position);
        checkCaseDesc.setArguments(bundle);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_frame,checkCaseDesc);
        ft.commit();
    }

    @Override
    public int getItemCount()
    {

        return checklist.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.image_crime)
        ImageView imageView;
        @BindView(R.id.tv_organization_name)
        TextView organization_name;
        @BindView(R.id.tv_case_description)
        TextView oranization_desc;
        @BindView(R.id.btn_case_detail)
        Button casedetail;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}
