package com.xbribe.ui.auth.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.xbribe.R;
import com.xbribe.data.models.User;
import com.xbribe.ui.auth.login.LoginFragment;
import com.xbribe.ui.auth.AuthenticationActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment
{
    @BindView(R.id.account_login)
    TextView login;

    @BindView(R.id.btn_register)
    MaterialButton btnRegister;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_confirmpassword)
    EditText etConfirmPassword;

    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private AuthenticationActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_register,container,false);
        ButterKnife.bind(this,parent);
        viewModel= ViewModelProviders.of(getActivity()).get(AuthenticationActivityViewModel.class);

        loginFragment= new LoginFragment();

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getRegisterResponse().observe(this, data->{
            if(data==null)
            {
                Toast.makeText(getActivity(),"Error! Please try again.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(), "User registered successfully", Toast.LENGTH_SHORT).show();
                fragmentManager = getActivity().getSupportFragmentManager();
                initFrag(loginFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @OnClick(R.id.account_login)
    void existingUser()
    {
        fragmentManager = getActivity().getSupportFragmentManager();
        initFrag(loginFragment);
    }

    @OnClick(R.id.btn_register)
    void register()
    {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmpassword = etConfirmPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter all the details!", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(password.equals(confirmpassword))
            {
                User user= new User(email,password);
                viewModel.userSignup(user);
            }
            else
            {
                Toast.makeText(getActivity(), "Passwords don't match!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initFrag(Fragment fragment) {

        FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack("Fragment2");
        ft.replace(R.id.frame_main, fragment);
        ft.commit();

    }
}
