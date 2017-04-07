/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

public abstract class BaseViewHolder<D> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private final OnItemClickListener<D> listener;

    protected D item;

    public BaseViewHolder(View itemView, OnItemClickListener<D> listener) {
        super(itemView);
        initViews();
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(item, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (listener != null) {
            listener.onItemLongClick(item, getAdapterPosition());
            return true;
        }
        return false;
    }

    public void setItem(D item) {
        this.item = item;
        bindItem();
    }

    protected abstract void initViews();

    protected abstract void bindItem();

}
