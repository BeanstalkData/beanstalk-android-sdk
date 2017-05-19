/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
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
import android.widget.EditText;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.model.TransactionEvent;
import com.beanstalkdata.android.response.TrackTransactionResponse;
import com.beanstalkdata.android.sample.BuildConfig;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.profile.adapter.TransactionEventsAdapter;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

import java.util.Arrays;

public class TransactionsFragment extends BaseFragment implements View.OnClickListener {

    private final TransactionEventsAdapter transactionEventsAdapter;

    private EditText transactionDetails;

    public TransactionsFragment() {
        this.transactionEventsAdapter = new TransactionEventsAdapter();
    }

    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        transactionDetails = (EditText) view.findViewById(R.id.transaction_details);
        view.findViewById(R.id.track_transaction).setOnClickListener(this);
        RecyclerView transactionsList = (RecyclerView) view.findViewById(R.id.transactions_list);
        transactionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionsList.setAdapter(transactionEventsAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateTransactions();
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
                updateTransactions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.transactions;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.track_transaction:
                trackTransaction();
                break;
        }
    }

    private void updateTransactions() {
        activityContract.showProgress();
        getService().getTransactionEvents(new OnReturnDataListener<TransactionEvent[]>() {
            @Override
            public void onFinished(TransactionEvent[] data, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    transactionEventsAdapter.updateList(Arrays.asList(data));
                } else {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

    private void trackTransaction() {
        String transactionDetailsText = transactionDetails.getText().toString();
        if (InputUtils.notEmpty(transactionDetailsText)) {
            activityContract.showProgress();
            getService().trackTransaction(BuildConfig.APP_USERNAME, transactionDetailsText, new OnReturnDataListener<TrackTransactionResponse>() {
                @Override
                public void onFinished(TrackTransactionResponse data, String error) {
                    if (activityContract != null) {
                        activityContract.hideProgress();
                    }
                    if (error == null) {
                        updateTransactions();
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

}