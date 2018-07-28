package com.whompum.PennyFlip.ActivitySourceList.Dialog;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;

public interface OnSourceCreated {
    void onSourceCreated(@NonNull final Source source);
}
