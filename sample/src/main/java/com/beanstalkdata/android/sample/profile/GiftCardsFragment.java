/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

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
import com.beanstalkdata.android.model.GiftCard;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;
import com.beanstalkdata.android.sample.dialog.RegisterNewGiftCardDialogFragment;
import com.beanstalkdata.android.sample.profile.adapter.GiftCardsAdapter;
import com.beanstalkdata.android.sample.utils.ToastUtils;

import java.util.Arrays;

public class GiftCardsFragment extends BaseFragment implements OnItemClickListener<GiftCard>, View.OnClickListener {

    private final GiftCardsAdapter giftCardsAdapter;

    public GiftCardsFragment() {
        this.giftCardsAdapter = new GiftCardsAdapter(this);
    }

    public static GiftCardsFragment newInstance() {
        return new GiftCardsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_cards, container, false);
        view.findViewById(R.id.register_new_gift_card).setOnClickListener(this);
        RecyclerView giftCardsList = (RecyclerView) view.findViewById(R.id.gift_cards_list);
        giftCardsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        giftCardsList.setAdapter(giftCardsAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateGiftCards();
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
                updateGiftCards();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.gift_cards;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_new_gift_card:
                if (activityContract != null) {
                    activityContract.showDialog(RegisterNewGiftCardDialogFragment.newInstance());
                }
                break;
        }
    }

    @Override
    public void onItemClick(final GiftCard item, final int position) {
        activityContract.showProgress();
        getService().inquireAboutCard(item.getNumber(), new OnReturnDataListener<String>() {
            @Override
            public void onFinished(String data, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    item.setBalance(data);
                    giftCardsAdapter.updateItem(item, position);
                } else {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

    @Override
    public void onItemLongClick(GiftCard item, int position) {

    }

    private void updateGiftCards() {
        activityContract.showProgress();
        getService().getGiftCardList(new OnReturnDataListener<GiftCard[]>() {
            @Override
            public void onFinished(GiftCard[] data, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    giftCardsAdapter.updateList(Arrays.asList(data));
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
