/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.model.type.MessageType;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseDialogFragment;
import com.beanstalkdata.android.sample.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateMessageStatusDialogFragment extends BaseDialogFragment implements DialogInterface.OnClickListener {

    private static final String KEY_MESSAGE_ID = "key_message_id";
    private static final String KEY_MESSAGE_CURRENT_STATUS = "key_message_current_status";
    private static final String KEY_MESSAGE_POSITION = "key_message_position";

    private String[] messageTypes;

    public static UpdateMessageStatusDialogFragment newInstance(String messageId, String messageCurrentStatus, int messagePosition, Fragment targetFragment) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_MESSAGE_ID, messageId);
        arguments.putString(KEY_MESSAGE_CURRENT_STATUS, messageCurrentStatus);
        arguments.putInt(KEY_MESSAGE_POSITION, messagePosition);
        UpdateMessageStatusDialogFragment dialogFragment = new UpdateMessageStatusDialogFragment();
        dialogFragment.setTargetFragment(targetFragment, 0);
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageTypes = getAvailableMessageTypes();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.update_message_status)
                .setItems(messageTypes, this)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void onClick(DialogInterface dialogInterface, int position) {
        final String newMessageStatus = messageTypes[position];
        String messageId = getArguments().getString(KEY_MESSAGE_ID);
        if (newMessageStatus != null && messageId != null) {
            activityContract.showProgress();
            getService().updateMessageStatus(messageId, newMessageStatus, new OnReturnDataListener<PushSuccessResponse>() {
                @Override
                public void onFinished(PushSuccessResponse data, String error) {
                    if (data.isSuccess()) {
                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null && targetFragment instanceof UpdateListener) {
                            ((UpdateListener) targetFragment).onStatusUpdate(newMessageStatus, getArguments().getInt(KEY_MESSAGE_POSITION));
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

    private String[] getAvailableMessageTypes() {
        List<String> messageTypes = new ArrayList<>(Arrays.asList(MessageType.READ, MessageType.UNREAD, MessageType.DELETED));
        messageTypes.remove(getArguments().getString(KEY_MESSAGE_CURRENT_STATUS));
        return messageTypes.toArray(new String[messageTypes.size()]);
    }

    public interface UpdateListener {

        void onStatusUpdate(String messageStatus, int messagePosition);

    }

}
