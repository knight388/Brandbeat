package com.mgrmobi.brandbeat.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@SuppressLint("ValidFragment")
public class AboutUsFragment_2 extends Fragment {
    @Bind(R.id.second_text)
    protected TextView textView;
    @Bind(R.id.thrid_text)
    protected TextView lastText;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_us_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        String text = " " + getString(R.string.second_text_about_us);
        SpannableString ss = new SpannableString(text);
        Drawable d = getResources().getDrawable(R.drawable.radio_button_margin);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(ss);

        SpannableString s1 = new SpannableString(" " + getString(R.string.thrid_text));
        span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        s1.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        lastText.setText(s1);

    }

}
