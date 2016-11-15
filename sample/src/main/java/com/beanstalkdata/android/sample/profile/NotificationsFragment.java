/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.profile.adapter.NotificationsAdapter;

public class NotificationsFragment extends BaseFragment {

    public static final String KEY_MESSAGE_ID = "key_message_id";

    private final NotificationsAdapter notificationsAdapter;

    public NotificationsFragment() {
        this.notificationsAdapter = new NotificationsAdapter();
    }

    public static NotificationsFragment newInstance(String messageId) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_MESSAGE_ID, messageId);

        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView messagesList = (RecyclerView) view.findViewById(R.id.notifications_list);
        messagesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesList.setAdapter(notificationsAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationsAdapter.updateList(PushNotificationsContainer.getInstance().getPushNotifications(getMessageId()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PushNotificationsContainer.getInstance().removePushNotifications(getMessageId());
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.notifications;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    private String getMessageId() {
        return getArguments().getString(KEY_MESSAGE_ID);
    }

}
