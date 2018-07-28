package com.whompum.PennyFlip.ActivitySourceList.Dialog;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by bryan on 1/24/2018.
 */

public class TextLimitWatcher implements TextWatcher {

    public static final int LIMIT = 20;

    private TextView subject;

    public TextLimitWatcher(@NonNull final TextView limitTextView){
        this.subject = limitTextView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length() <= LIMIT) {

            final String limitDisplay = String.valueOf(s.length()) + "/" +
                    String.valueOf(LIMIT);

            //Update the limit display by showing 0/20 - 20/20
            subject.setText(limitDisplay);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}









