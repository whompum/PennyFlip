package com.whompum.PennyFlip.SourceDialog;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by bryan on 12/21/2017.
 */

public abstract class SearchWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public abstract void afterTextChanged(Editable s);


}
