package com.beanstalkdata.android.sample.profile;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;

@SuppressLint("SetJavaScriptEnabled")
public class ContactAssetFragment extends BaseFragment {

    protected static final String KEY_URL = "key_url";
    protected static final String KEY_TITLE = "key_title";

    private int title = 0;

    public static ContactAssetFragment newInstance(String url, @StringRes int title) {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        args.putInt(KEY_TITLE, title);
        ContactAssetFragment fragment = new ContactAssetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asset, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        title = getArguments().getInt(KEY_TITLE, 0);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setGeolocationEnabled(false);
        webView.loadUrl(getArguments().getString(KEY_URL));
    }

    @Override
    protected int getTitleResourceId() {
        return title > 0 ? title : R.string.contact_asset;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

}
