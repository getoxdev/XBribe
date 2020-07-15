package com.xbribe.ui.main.drawers.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{
    List<NotificationModel> nlist;
    Context context;

    public NotificationAdapter(List<NotificationModel> nlist, Context context)
    {
        this.nlist = nlist;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position)
    {
        holder.organisation_name.setText(nlist.get(position).getOrganisation_name());
        holder.organisation_ministry.setText(nlist.get(position).getOrganisation_ministry());
        holder.organisation_department.setText(nlist.get(position).getOrganisationdepartmnt());
        holder.case_id.setText(nlist.get(position).getCase_id());
        holder.user_id.setText(nlist.get(position).getUser_id());
        holder.notification.setText(nlist.get(position).getNotification_msg());

    }

    @Override
    public int getItemCount() {
        return nlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_organisation_name)
        TextView organisation_name;
        @BindView(R.id.tv_organisation_ministry)
        TextView organisation_ministry;
        @BindView(R.id.tv_organisation_department)
        TextView organisation_department;
        @BindView(R.id.tv_case_id)
        TextView case_id;
        @BindView(R.id.tv_user_id)
        TextView  user_id;
        @BindView(R.id.tv_notification_msg)
        TextView notification;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
