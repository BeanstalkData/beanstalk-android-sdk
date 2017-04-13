/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile.adapter;

import android.view.View;
import android.widget.TextView;

import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseRecyclerViewAdapter;
import com.beanstalkdata.android.sample.base.BaseViewHolder;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessagesAdapter extends BaseRecyclerViewAdapter<PushMessage, MessagesAdapter.MessageHolder> {

    public MessagesAdapter(OnItemClickListener<PushMessage> listener) {
        super(listener);
    }

    @Override
    protected int getViewHolderLayoutResourceId() {
        return R.layout.item_message;
    }

    @Override
    protected MessageHolder getViewHolder(View view, OnItemClickListener<PushMessage> listener) {
        return new MessageHolder(view, listener);
    }

    public void updateStatus(String status, int position) {
        dataList.get(position).setStatus(status);
        notifyItemChanged(position);
    }

    static class MessageHolder extends BaseViewHolder<PushMessage> {

        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        private TextView id;
        private TextView category;
        private TextView sentDate;
        private TextView statusAndUpdatedDate;

        public MessageHolder(View itemView, OnItemClickListener<PushMessage> listener) {
            super(itemView, listener);
        }

        @Override
        protected void initViews() {
            id = (TextView) itemView.findViewById(R.id.message_id);
            category = (TextView) itemView.findViewById(R.id.message_category);
            sentDate = (TextView) itemView.findViewById(R.id.message_sent_date);
            statusAndUpdatedDate = (TextView) itemView.findViewById(R.id.message_status_and_updated_date);
        }

        @Override
        protected void bindItem() {
            if (item != null) {
                id.setText(item.getStepId());
                category.setText(item.getCategory());
                sentDate.setText(DATE_FORMAT.format(item.getUpdatedAt()));
                statusAndUpdatedDate.setText(itemView.getResources().getString(R.string.message_status_format, item.getStatus(), DATE_FORMAT.format(item.getUpdatedAt())));
            }
        }

    }

}
