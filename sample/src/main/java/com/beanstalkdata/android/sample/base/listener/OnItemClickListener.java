package com.beanstalkdata.android.sample.base.listener;

public interface OnItemClickListener<D> {

    void onItemClick(D item, int position);

    void onItemLongClick(D item, int position);

}
