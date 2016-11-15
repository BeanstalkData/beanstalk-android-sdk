package com.beanstalkdata.android.sample.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.profile.adapter.RewardsAdapter;
import com.beanstalkdata.android.sample.utils.ToastUtils;

import java.util.Arrays;

public class RewardsFragment extends BaseFragment {

    private final RewardsAdapter rewardsAdapter;

    public RewardsFragment() {
        this.rewardsAdapter = new RewardsAdapter();
    }

    public static RewardsFragment newInstance() {
        return new RewardsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        RecyclerView rewardsList = (RecyclerView) view.findViewById(R.id.rewards_list);
        rewardsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rewardsList.setAdapter(rewardsAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateRewards();
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
                updateRewards();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.rewards;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    private void updateRewards() {
        activityContract.showProgress();
        getService().getUserOffers(new OnReturnDataListener<Coupon[]>() {
            @Override
            public void onFinished(Coupon[] data, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    rewardsAdapter.updateList(Arrays.asList(data));
                } else {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

}
