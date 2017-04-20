/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile.adapter;

import android.view.View;
import android.widget.TextView;

import com.beanstalkdata.android.model.TransactionEvent;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseRecyclerViewAdapter;
import com.beanstalkdata.android.sample.base.BaseViewHolder;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransactionEventsAdapter extends BaseRecyclerViewAdapter<TransactionEvent, TransactionEventsAdapter.TransactionEventHolder> {

    @Override
    protected int getViewHolderLayoutResourceId() {
        return R.layout.item_transaction_event;
    }

    @Override
    protected TransactionEventHolder getViewHolder(View view, OnItemClickListener<TransactionEvent> listener) {
        return new TransactionEventHolder(view);
    }

    static class TransactionEventHolder extends BaseViewHolder<TransactionEvent> {

        private final SimpleDateFormat transactionDateFormat;

        private TextView transactionId;
        private TextView transactionDate;
        private TextView transactionStatus;

        public TransactionEventHolder(View itemView) {
            super(itemView, null);
            this.transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.US);
        }

        @Override
        protected void initViews() {
            transactionId = (TextView) itemView.findViewById(R.id.transaction_id);
            transactionDate = (TextView) itemView.findViewById(R.id.transaction_date);
            transactionStatus = (TextView) itemView.findViewById(R.id.transaction_status);
        }

        @Override
        protected void bindItem() {
            if (item == null) {
                return;
            }

            transactionId.setText(item.getTransactionId());
            transactionDate.setText(transactionDateFormat.format(item.getDate().getDate()));
            transactionStatus.setText(item.getStatus());
        }

    }

}