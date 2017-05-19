/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanstalkdata.android.sample.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<D, VH extends BaseViewHolder<D>> extends RecyclerView.Adapter<VH> {

    protected final List<D> dataList;
    private final OnItemClickListener<D> listener;

    public BaseRecyclerViewAdapter() {
        this(null);
    }

    public BaseRecyclerViewAdapter(OnItemClickListener<D> listener) {
        this.dataList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getViewHolderLayoutResourceId(), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.setItem(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @LayoutRes
    protected abstract int getViewHolderLayoutResourceId();

    protected abstract VH getViewHolder(View view, OnItemClickListener<D> listener);

    public void updateItem(D item, int position) {
        dataList.set(position, item);
        notifyItemChanged(position);
    }

    public void updateList(List<D> newDataList) {
        dataList.clear();
        dataList.addAll(newDataList);
        notifyDataSetChanged();
    }

}
