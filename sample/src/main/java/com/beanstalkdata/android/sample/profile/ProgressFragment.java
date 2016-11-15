package com.beanstalkdata.android.sample.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class ProgressFragment extends BaseFragment {

    private TextView progressValue;

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        progressValue = (TextView) view.findViewById(R.id.progress_value);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateProgress();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                updateProgress();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.progress;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    private void updateProgress() {
        activityContract.showProgress();
        getService().getProgress(new OnReturnDataListener<Double>() {
            @Override
            public void onFinished(Double count, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (count == null) {
                    FragmentActivity activity = getActivity();
                    if ((activity != null) && (error != null)) {
                        ToastUtils.showLong(activity, error);
                    }
                } else {
                    if (progressValue != null) {
                        progressValue.setText(getString(R.string.rewards_count, count.intValue()));
                    }
                }
            }
        });
    }

}
