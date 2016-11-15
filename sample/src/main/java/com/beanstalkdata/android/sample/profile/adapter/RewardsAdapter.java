/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseRecyclerViewAdapter;
import com.beanstalkdata.android.sample.base.BaseViewHolder;
import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RewardsAdapter extends BaseRecyclerViewAdapter<Coupon, RewardsAdapter.RewardViewHolder> {

    @Override
    protected int getViewHolderLayoutResourceId() {
        return R.layout.item_reward;
    }

    @Override
    protected RewardViewHolder getViewHolder(View view, OnItemClickListener<Coupon> listener) {
        return new RewardViewHolder(view, listener);
    }

    static class RewardViewHolder extends BaseViewHolder<Coupon> {

        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        private TextView name;
        private TextView expiredDate;
        private ImageView image;

        public RewardViewHolder(View itemView, OnItemClickListener<Coupon> listener) {
            super(itemView, listener);
        }

        @Override
        protected void initViews() {
            name = (TextView) itemView.findViewById(R.id.reward_name);
            expiredDate = (TextView) itemView.findViewById(R.id.reward_expired_date);
            image = (ImageView) itemView.findViewById(R.id.reward_image);
        }

        @Override
        protected void bindItem() {
            if (item == null) {
                return;
            }

            name.setText(item.getText());
            Date expiredDate = item.getExpired();
            if (expiredDate != null) {
                this.expiredDate.setText(DATE_FORMAT.format(expiredDate));
            }
            Picasso.with(itemView.getContext()).load(item.getImgUrl()).into(image);
        }

    }

}
