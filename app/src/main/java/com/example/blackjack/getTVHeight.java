package com.example.blackjack;

import android.text.Layout;
import android.widget.TextView;

public class getTVHeight {
    public int getTextViewHeight(TextView view) {
        Layout layout = view.getLayout();
        int desired = layout.getLineTop(view.getLineCount());
        int padding = view.getCompoundPaddingTop() + view.getCompoundPaddingBottom();
        return desired + padding;
    }

}
