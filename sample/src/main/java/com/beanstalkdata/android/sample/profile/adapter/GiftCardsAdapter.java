/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile.adapter;

import android.view.View;
import android.widget.TextView;

import com.beanstalkdata.android.model.GiftCard;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseRecyclerViewAdapter;
import com.beanstalkdata.android.sample.base.BaseViewHolder;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

public class GiftCardsAdapter extends BaseRecyclerViewAdapter<GiftCard, GiftCardsAdapter.GiftCardViewHolder> {

    public GiftCardsAdapter(OnItemClickListener<GiftCard> listener) {
        super(listener);
    }

    @Override
    protected int getViewHolderLayoutResourceId() {
        return R.layout.item_gift_card;
    }

    @Override
    protected GiftCardViewHolder getViewHolder(View view, OnItemClickListener<GiftCard> listener) {
        return new GiftCardViewHolder(view, listener);
    }

    static class GiftCardViewHolder extends BaseViewHolder<GiftCard> {

        private TextView cardNumber;
        private TextView cardBalance;

        public GiftCardViewHolder(View itemView, OnItemClickListener<GiftCard> listener) {
            super(itemView, listener);
        }

        @Override
        protected void initViews() {
            cardNumber = (TextView) itemView.findViewById(R.id.gift_card_number);
            cardBalance = (TextView) itemView.findViewById(R.id.gift_card_balance);
        }

        @Override
        protected void bindItem() {
            if (item == null) {
                return;
            }

            cardNumber.setText(item.getDisplayNumber());
            cardBalance.setText(item.getDisplayBalance());
        }

    }

}
