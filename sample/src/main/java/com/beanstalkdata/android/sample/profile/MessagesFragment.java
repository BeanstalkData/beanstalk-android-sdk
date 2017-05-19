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

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.response.PushMessagesResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;
import com.beanstalkdata.android.sample.dialog.UpdateMessageStatusDialogFragment;
import com.beanstalkdata.android.sample.profile.adapter.MessagesAdapter;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class MessagesFragment extends BaseFragment implements OnItemClickListener<PushMessage>, UpdateMessageStatusDialogFragment.UpdateListener {

    public static final int MAX_MESSAGES_COUNT = 100;

    private final MessagesAdapter messagesAdapter;

    public MessagesFragment() {
        this.messagesAdapter = new MessagesAdapter(this);
    }

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        RecyclerView messagesList = (RecyclerView) view.findViewById(R.id.messages_list);
        messagesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesList.setAdapter(messagesAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateMessages();
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
                updateMessages();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.messages;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public void onItemClick(PushMessage item, int position) {
        String messageId = item.getId();
        if (messageId != null && activityContract != null) {
            activityContract.replaceFragment(MessageDetailsFragment.newInstance(messageId));
        }
    }

    @Override
    public void onItemLongClick(PushMessage item, int position) {
        String messageId = item.getId();
        if (messageId != null && activityContract != null) {
            activityContract.showDialog(UpdateMessageStatusDialogFragment.newInstance(messageId, item.getStatus(), position, this));
        }
    }

    @Override
    public void onStatusUpdate(String messageStatus, int messagePosition) {
        if (activityContract != null) {
            activityContract.hideProgress();
        }
        messagesAdapter.updateStatus(messageStatus, messagePosition);
    }

    private void updateMessages() {
        activityContract.showProgress();
        getService().getContactMessages(MAX_MESSAGES_COUNT, new OnReturnDataListener<PushMessagesResponse>() {
            @Override
            public void onFinished(PushMessagesResponse data, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    if (data != null) {
                        messagesAdapter.updateList(data.getPushMessages());
                    }
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
