package com.xbribe.ui.main.drawers.checkcase.checkcasedesc;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckCaseDescAdapter  extends RecyclerView.Adapter<CheckCaseDescAdapter.ViewHolder>
{

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tv_organization_name)
        TextView organisation_name;
        @BindView(R.id.tv_organization_category)
        TextView organisation_category;
        @BindView(R.id.tv_case_process)
        TextView caseprocess;
        @BindView(R.id.tv_case_description)
        TextView case_description;
        @BindView(R.id.tv_location)
        TextView location;
        @BindView(R.id.tv_images_no)
        TextView images_no;
        @BindView(R.id.tv_audio_no)
        TextView audio_no;
        @BindView(R.id.tv_video_no)
        TextView video_no;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
