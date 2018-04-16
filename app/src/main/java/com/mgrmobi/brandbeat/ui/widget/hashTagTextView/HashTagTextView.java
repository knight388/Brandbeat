package com.mgrmobi.brandbeat.ui.widget.hashTagTextView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mgrmobi.brandbeat.ui.activity.HashTagActivity;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class HashTagTextView extends TextView {
    public HashTagTextView(final Context context) {
        super(context);
    }

    public HashTagTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public HashTagTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HashTagTextView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setText(String text)
    {
        if(text == null) return;
        String hashTag = "";
        if(getContext() instanceof HashTagActivity)
        {
            hashTag = ((HashTagActivity) getContext()).getIntent().getStringExtra(HashTagActivity.HASH_TAG);
        }
        SpannableString resultString = new SpannableString(text);
        int indexHashTag = 0;
        for(int i = 0; i< text.length() - text.replaceAll("#", "").length(); i++)
        {
            indexHashTag = text.indexOf("#", indexHashTag);
            if(indexHashTag < 0) break;
            int nextSpace = text.indexOf(" ", indexHashTag);

            if(nextSpace > 0)
            {
                final int finalIndexHashTag = indexHashTag;
                final int finalIndexHashTag2 = indexHashTag;
                final String finalHashTag = hashTag;
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        if(finalHashTag.equals(text.substring(finalIndexHashTag2 + 1, nextSpace))) return;
                        getContext().startActivity(HashTagActivity.createIntent(getContext(), text.substring(finalIndexHashTag+1, nextSpace)));
                    }
                };
                resultString.setSpan(clickableSpan, indexHashTag, nextSpace, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else
            {
                final int finalIndexHashTag1 = indexHashTag;
                final String finalHashTag1 = hashTag;
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        if(finalHashTag1.equals(text.substring(finalIndexHashTag1 + 1, text.length()))) return;
                        getContext().startActivity(HashTagActivity.createIntent(getContext(), text.substring(finalIndexHashTag1+1, text.length())));
                    }
                };
                resultString.setSpan(clickableSpan, indexHashTag, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            indexHashTag = nextSpace;
        }
        super.setText(resultString);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
}
