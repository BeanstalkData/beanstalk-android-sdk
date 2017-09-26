/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.contract.ActivityContract;

public abstract class BaseActivity extends AppCompatActivity implements ActivityContract {

    private FrameLayout fragmentContainer;
    private ProgressBar backgroundOpsProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        backgroundOpsProgress = (ProgressBar) findViewById(R.id.background_ops_progress);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, getInitFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showDialog(DialogFragment dialogFragment) {
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    @Override
    public void popBack() {
        onBackPressed();
    }

    @Override
    public void showProgress() {
        fragmentContainer.setVisibility(View.GONE);
        backgroundOpsProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        fragmentContainer.setVisibility(View.VISIBLE);
        backgroundOpsProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected abstract Fragment getInitFragment();

}
