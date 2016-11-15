/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.base.listener;

public interface OnItemClickListener<D> {

    void onItemClick(D item, int position);

    void onItemLongClick(D item, int position);

}
