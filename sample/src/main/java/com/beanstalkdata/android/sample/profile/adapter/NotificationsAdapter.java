/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile.adapter;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.beanstalkdata.android.model.PushNotification;
import com.beanstalkdata.android.model.type.MessageContentType;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseRecyclerViewAdapter;
import com.beanstalkdata.android.sample.base.BaseViewHolder;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

public class NotificationsAdapter extends BaseRecyclerViewAdapter<PushNotification, NotificationsAdapter.MessageHolder> {

    @Override
    protected int getViewHolderLayoutResourceId() {
        return R.layout.item_notification;
    }

    @Override
    protected MessageHolder getViewHolder(View view, OnItemClickListener<PushNotification> listener) {
        return new MessageHolder(view, listener);
    }

    static class MessageHolder extends BaseViewHolder<PushNotification> {

        private TextView title;
        private TextView text;
        private TextView os;

        public MessageHolder(View itemView, OnItemClickListener<PushNotification> listener) {
            super(itemView, listener);
        }

        @Override
        protected void initViews() {
            title = (TextView) itemView.findViewById(R.id.notification_title);
            text = (TextView) itemView.findViewById(R.id.notification_text);
            os = (TextView) itemView.findViewById(R.id.notification_os);
        }

        @Override
        protected void bindItem() {
            if (item == null) {
                return;
            }

            title.setText(item.getTitle());
            text.setText(getItemText());
            os.setText(item.getOperationSystem());
        }

        @SuppressWarnings("deprecation")
        private CharSequence getItemText() {
            switch (item.getType()) {
                case MessageContentType.STANDARD:
                    return item.getContent();
                case MessageContentType.HTML:
                    return Html.fromHtml(item.getBody());
                default:
                    return null;
            }
        }

    }

}
