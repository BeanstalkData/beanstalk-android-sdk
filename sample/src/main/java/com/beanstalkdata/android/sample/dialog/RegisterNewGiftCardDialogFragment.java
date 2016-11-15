package com.beanstalkdata.android.sample.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseDialogFragment;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class RegisterNewGiftCardDialogFragment extends BaseDialogFragment implements DialogInterface.OnShowListener, View.OnClickListener {

    private EditText giftCardNumber;
    private EditText giftCardPin;

    public static RegisterNewGiftCardDialogFragment newInstance() {
        return new RegisterNewGiftCardDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_register_new_gift_card, null);
        giftCardNumber = (EditText) view.findViewById(R.id.gift_card_number);
        giftCardPin = (EditText) view.findViewById(R.id.gift_card_pin);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.register_new_gift_card)
                .setView(view)
                .setPositiveButton(R.string.register, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        Dialog dialog = getDialog();
        if (dialog instanceof AlertDialog) {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        String giftCardNumberValue = giftCardNumber.getText().toString();
        String giftCardPinValue = giftCardPin.getText().toString();
        if (InputUtils.notEmpty(giftCardNumberValue)) {
            activityContract.showProgress();
            getService().registerNewGiftCard(giftCardNumberValue, giftCardPinValue, new OnReturnListener() {
                @Override
                public void onFinished(String error) {
                    if (activityContract != null) {
                        activityContract.hideProgress();
                    }
                    if (error == null) {
                        dismiss();
                    } else {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            ToastUtils.showLong(activity, error);
                        }
                    }
                }
            });
        } else {
            ToastUtils.showLong(getActivity(), R.string.error_gift_card_number_not_empty);
        }
    }

}
