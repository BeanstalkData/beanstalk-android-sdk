package com.beanstalkdata.android.sample.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.profile.ProfileActivity;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class MenuFragment extends BaseFragment implements View.OnClickListener {

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        view.findViewById(R.id.sign_in).setOnClickListener(this);
        view.findViewById(R.id.sign_up).setOnClickListener(this);
        view.findViewById(R.id.reset_password).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityContract.showProgress();
        getService().checkUserSession(new OnReturnDataListener<Boolean>() {
            @Override
            public void onFinished(Boolean isSuccessful, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (isSuccessful) {
                    if (activityContract != null) {
                        activityContract.startActivity(ProfileActivity.class);
                    }
                } else {
                    FragmentActivity activity = getActivity();
                    if (activity != null && error != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.menu;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return false;
    }

    @Override
    public void onClick(View button) {
        switch (button.getId()) {
            case R.id.sign_in:
                if (activityContract != null) {
                    activityContract.replaceFragment(SignInFragment.newInstance());
                }
                break;
            case R.id.sign_up:
                if (activityContract != null) {
                    activityContract.replaceFragment(ContactInfoFragment.signUp());
                }
                break;
            case R.id.reset_password:
                if (activityContract != null) {
                    activityContract.replaceFragment(ResetPasswordFragment.newInstance());
                }
                break;
        }
    }
}
