package com.whompum.PennyFlip.ActivitySourceList;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public interface SourceListControllerClient {

    enum QueryOp{
        NO_OP,
        DATA_ADD,
        DATA_SPEND,
        QUERIED_LIKE_TITLE,
    }

    void onDataQueried();
    void onDataUpdate(@NonNull final List<Source> data, @NonNull final QueryOp op);
    void onSaveResult(final boolean successful, @Nullable Integer reason);
}
