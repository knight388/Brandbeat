package com.mgrmobi.brandbeat.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgrmobi.brandbeat.R;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@SuppressLint("ValidFragment")
public class CustomProgressDialog  extends DialogFragment
{
    private Context mContext;

    public CustomProgressDialog(Context context)
    {
        setStyle(STYLE_NO_TITLE, STYLE_NORMAL);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.progress_dialog, container, false);
        setCancelable(false);
        return view;
    }
}