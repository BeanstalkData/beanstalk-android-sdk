/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.response.PushMessageByIdResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageDetailsFragment extends BaseFragment {

    public static final String KEY_MESSAGE_ID = "key_message_id";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private TextView messageId;
    private TextView messageCampaignId;
    private TextView messageContactId;
    private TextView messageCustomerId;
    private TextView messageStepId;
    private TextView messageUpdatedAt;
    private TextView messageTitle;
    private TextView messageSubtitle;
    private TextView messageInboxTitle;
    private TextView messageOs;
    private TextView messageStatus;
    private TextView messageCategory;
    private TextView messageAppHook;
    private TextView messageUrl;
    private TextView messageThumbnailUrl;
    private TextView messageType;
    private TextView messageBody;

    public static MessageDetailsFragment newInstance(String messageId) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_MESSAGE_ID, messageId);

        MessageDetailsFragment fragment = new MessageDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_details, container, false);
        messageId = (TextView) view.findViewById(R.id.message_id);
        messageCampaignId = (TextView) view.findViewById(R.id.message_campaign_id);
        messageContactId = (TextView) view.findViewById(R.id.message_contact_id);
        messageCustomerId = (TextView) view.findViewById(R.id.message_customer_id);
        messageStepId = (TextView) view.findViewById(R.id.message_step_id);
        messageUpdatedAt = (TextView) view.findViewById(R.id.message_updated_at);
        messageTitle = (TextView) view.findViewById(R.id.message_title);
        messageSubtitle = (TextView) view.findViewById(R.id.message_subtitle);
        messageInboxTitle = (TextView) view.findViewById(R.id.message_inbox_title);
        messageOs = (TextView) view.findViewById(R.id.message_os);
        messageStatus = (TextView) view.findViewById(R.id.message_status);
        messageCategory = (TextView) view.findViewById(R.id.message_category);
        messageAppHook = (TextView) view.findViewById(R.id.message_app_hook);
        messageUrl = (TextView) view.findViewById(R.id.message_url);
        messageThumbnailUrl = (TextView) view.findViewById(R.id.message_thumbnail_url);
        messageType = (TextView) view.findViewById(R.id.message_type);
        messageBody = (TextView) view.findViewById(R.id.message_body);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateMessageDetails();
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.message_details;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    private String getMessageId() {
        return getArguments().getString(KEY_MESSAGE_ID);
    }

    private void updateMessageDetails() {
        activityContract.showProgress();
        getService().getMessageById(getMessageId(), new OnReturnDataListener<PushMessageByIdResponse>() {
            @Override
            public void onFinished(PushMessageByIdResponse response, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                PushMessage message = response.getPushMessage();
                if (getView() != null && message != null) {
                    messageId.setText(message.getId());
                    messageCampaignId.setText(message.getCampaignId());
                    messageContactId.setText(message.getContactId());
                    messageCustomerId.setText(message.getCustomerId());
                    messageStepId.setText(message.getStepId());
                    messageUpdatedAt.setText(DATE_FORMAT.format(message.getUpdatedAt()));
                    messageTitle.setText(message.getTitle());
                    messageSubtitle.setText(message.getSubTitle());
                    messageInboxTitle.setText(message.getInboxTitle());
                    messageOs.setText(message.getOs());
                    messageStatus.setText(message.getStatus());
                    messageCategory.setText(message.getCategory());
                    messageAppHook.setText(message.getAppHook());
                    messageUrl.setText(message.getMessageUrl());
                    messageThumbnailUrl.setText(message.getThumbnailUrl());
                    messageType.setText(message.getMessageType());
                    messageBody.setText(message.getMessageBody());
                }
            }
        });
    }

}
