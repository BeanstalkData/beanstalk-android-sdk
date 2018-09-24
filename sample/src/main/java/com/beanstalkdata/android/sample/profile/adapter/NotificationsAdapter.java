/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile.adapter;

import android.view.View;
import android.widget.TextView;

import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseRecyclerViewAdapter;
import com.beanstalkdata.android.sample.base.BaseViewHolder;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

public class NotificationsAdapter extends BaseRecyclerViewAdapter<PushMessage, NotificationsAdapter.MessageHolder> {

    @Override
    protected int getViewHolderLayoutResourceId() {
        return R.layout.item_notification;
    }

    @Override
    protected MessageHolder getViewHolder(View view, OnItemClickListener<PushMessage> listener) {
        return new MessageHolder(view, listener);
    }

    static class MessageHolder extends BaseViewHolder<PushMessage> {

        private TextView title;
        private TextView os;

        public MessageHolder(View itemView, OnItemClickListener<PushMessage> listener) {
            super(itemView, listener);
        }

        @Override
        protected void initViews() {
            title = (TextView) itemView.findViewById(R.id.notification_title);
            os = (TextView) itemView.findViewById(R.id.notification_os);
        }

        @Override
        protected void bindItem() {
            if (item == null) {
                return;
            }

            title.setText(item.getTitle());
            os.setText(item.getOs());
        }

    }

}
