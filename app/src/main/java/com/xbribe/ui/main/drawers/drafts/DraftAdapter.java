package com.xbribe.ui.main.drawers.drafts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;
import com.xbribe.ui.function.Step_one_Fragment;
import com.xbribe.ui.function.Step_two_Fragment;
import com.xbribe.ui.function.SubmissionActivity;
import com.xbribe.ui.main.drawers.checkcase.checkcasedesc.CheckCaseDescFragment;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.Viewholder>
{

    FragmentManager fragmentManager;

    private View.OnClickListener onItemClickListener;


   List<DraftModel> draftModelList;

   Context context;

    public DraftAdapter(List<DraftModel> draftModelList, Context context)
    {
        this.draftModelList = draftModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public DraftAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_draft,parent,false);
        return new Viewholder(view);


    }
    public void setOnItemClickListener(View.OnClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DraftAdapter.Viewholder holder, int position)
    {
        holder.ministry.setText(draftModelList.get(position).getMinistry());
        holder.address.setText(draftModelList.get(position).getAddress());
        holder.pincode.setText(draftModelList.get(position).getPincode());
        holder.city.setText(draftModelList.get(position).getCity());
        holder.department.setText(draftModelList.get(position).getDeparment());
        holder.organisationname.setText(draftModelList.get(position).getOrganisation_name());
        holder.casedescription.setText(draftModelList.get(position).getDescription());

    }

    @Override
    public int getItemCount()
    {
       return draftModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_ministry)
        TextView ministry;

        @BindView(R.id.tv_address)
        TextView address;

        @BindView(R.id.tv_pincode)
        TextView pincode;

        @BindView(R.id.tv_city)
        TextView city;

        @BindView(R.id.tv_department)
        TextView department;

        @BindView(R.id.tv_organisation_name)
        TextView organisationname;

        @BindView(R.id.tv_case_description)
        TextView casedescription;



        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setTag(this);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}