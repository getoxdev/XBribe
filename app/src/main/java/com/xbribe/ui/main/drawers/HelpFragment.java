package com.xbribe.ui.main.drawers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xbribe.R;

import butterknife.ButterKnife;

public class HelpFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent=inflater.inflate(R.layout.fragment_help,container,false);
        ButterKnife.bind(this,parent);
        return parent;

    }
}
