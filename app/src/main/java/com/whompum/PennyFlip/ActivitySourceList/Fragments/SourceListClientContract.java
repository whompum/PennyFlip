package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public interface SourceListClientContract {
    void display(@NonNull final List<Source> data);
    void onNoData();
}
