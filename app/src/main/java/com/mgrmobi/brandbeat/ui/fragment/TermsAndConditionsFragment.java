package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.mgrmobi.brandbeat.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class TermsAndConditionsFragment extends Fragment {
    @Bind(R.id.webView)
    public WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.terms_and_coditions_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        StringBuilder buf = new StringBuilder();
        String str = "";
        InputStream json = null;
        try {
            json = getActivity().getAssets().open("terms_and_conditions");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));

            while((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //webView.loadUrl("file:///android_asset/terms_and_conditions");
        webView.loadData(String.valueOf(buf), "text/html; charset=UTF-8", null);
        //webView.loadDataWithBaseURL(null, "file:///android_asset/terms_and_conditions", "text/html", "utf-8", null);
    }
}
