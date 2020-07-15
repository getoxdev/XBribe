package com.xbribe.ui.function;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.xbribe.R;
import com.xbribe.service.AddressService;
import com.xbribe.ui.auth.AuthenticationActivityViewModel;
import com.xbribe.ui.main.ReportFragment;
import com.xbribe.ui.main.drawers.ContactFragment;
import com.xbribe.ui.main.drawers.aboutus.AboutUsFragment;
import com.xbribe.ui.main.drawers.checkcase.CheckcaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubmissionActivity  extends AppCompatActivity
{


    FragmentManager fragmentManager;
    Step_one_Fragment step_one_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        step_one_fragment=new Step_one_Fragment();
        initFrag(step_one_fragment);


    }

   private void initFrag(Fragment fragment) {

       FragmentTransaction ft = fragmentManager.beginTransaction();
       ft.setCustomAnimations(android.R.anim.fade_in,
               android.R.anim.fade_out);
       ft.replace(R.id.main_frame_two, fragment);
       ft.commit();

   }
}
