package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.base.ContainerAddBrend;
import com.mgrmobi.brandbeat.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SendFeedBackFragment extends Fragment {
    @Bind(R.id.first_subject)
    public EditText subjectFirst;
    @Bind(R.id.message)
    public EditText message;
    @Bind(R.id.message_input_layout)
    public TextInputLayout textMessageInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_feedback_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.send_feedback)
    public void onClick() {
        if(message.getText().toString().equals(""))
        {
            textMessageInputLayout.setError(getString(R.string.required));
            return;
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", getString(R.string.feedback_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectFirst.getText().toString());
        String body = "";
//        body += Utils.getDeviceName() + "\n";
//        body += getString(R.string.android) + " " + android.os.Build.VERSION.RELEASE + "\n";
//        body += Utils.getVersion() + "\n";
//        body += message.getText().toString();
//        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        body += "<html><h3>" + Utils.getDeviceName() + "</h3>";
        body += "<h3>" + getString(R.string.android) + " " + android.os.Build.VERSION.RELEASE + "</h3>";
        body += "<h3>" + Utils.getVersion() + "</h3>";
        body += "<h3>" + message.getText().toString() + "</h3></html>";;
        emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body);

        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
    }
}
