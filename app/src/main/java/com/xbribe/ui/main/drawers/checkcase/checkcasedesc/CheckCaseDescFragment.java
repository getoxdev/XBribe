package com.xbribe.ui.main.drawers.checkcase.checkcasedesc;

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

import com.xbribe.R;
import com.xbribe.ui.function.DatabaseHelper;
import com.xbribe.ui.main.drawers.checkcase.CheckcaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckCaseDescFragment extends Fragment
{
    @BindView(R.id.btn_back)
    Button back;
    @BindView(R.id.tv_organization_name)//
    TextView organization_name;
    @BindView(R.id.tv_case_description)//description
    TextView case_description;
    @BindView(R.id.tv_organization_category)//ministry
    TextView organization_category;
    @BindView(R.id.tv_location)
    TextView location;
    @BindView(R.id.tv_images_no)
    TextView imageno;
    @BindView(R.id.tv_audio_no)
    TextView audiono;
    @BindView(R.id.tv_video_no)
    TextView videono;
    //8  column
    FragmentManager fragmentManager;

    CheckcaseFragment checkcaseFragment;

    List<CheckcasedescModel> casedetails;

    int position;

    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_checkdesc_case, container, false);
        ButterKnife.bind(this,parent);
        databaseHelper=new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();
        position =getArguments().getInt("Position passed");
        displaydetails(position);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                initfrag();

            }
        });
        return parent;
    }

    private void displaydetails(int position)
    {
        Cursor cursor=databaseHelper.getrowdetails(position);

        if(cursor.getCount()==0)
        {
            showMessage("Error","Nothing found");
        }
        if (cursor.moveToFirst())
        {
            do {
                location.setText(cursor.getString(2));
                case_description.setText(cursor.getString(3));
                organization_name.setText(cursor.getString(4));
                organization_category.setText(cursor.getString(5));
                imageno.setText(cursor.getString(7));
                audiono.setText(cursor.getString(8));
                videono.setText(cursor.getString(9));

            } while (cursor.moveToNext());
        }

    }

    private void initfrag()
    {
        checkcaseFragment=new CheckcaseFragment();
        fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_frame,checkcaseFragment);
        ft.commit();
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
