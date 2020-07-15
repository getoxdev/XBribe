package com.xbribe.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.xbribe.R;

public class SliderAdapter extends  SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

private Context context;

public SliderAdapter(Context context) {
        this.context = context;
        }

@Override
public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases, null);
        return new SliderAdapterVH(inflate);
        }

@Override
public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        switch (position) {
            case 0:
                Glide.with(viewHolder.itemView)
                        .load("https://www.deccanherald.com/sites/dh/files/styles/article_detail/public/article_images/2018/07/17/inflation-pti1531817413.jpg?itok=x6TRI-rP")
                        .into(viewHolder.imageViewBackground);
                break;

            case 1:
                Glide.with(viewHolder.itemView)
                        .load("https://www.engageinlearning.com/wp-content/uploads/What-is-Anti-Bribery-and-Corruption.jpg")
                        .into(viewHolder.imageViewBackground);
                break;

            default:
                Glide.with(viewHolder.itemView)
                        .load("https://postcard.news/wp-content/uploads/2016/08/supreme-court1-1170x610.jpg")
                        .into(viewHolder.imageViewBackground);
                break;

        }
}
@Override
public int getCount()
       {
        //slider view count could be dynamic size
        return 3;
        }

class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

    View itemView;
    ImageView imageViewBackground;
    TextView textViewDescription;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.imageView);
        this.itemView = itemView;
    }
}
}
