package com.whompum.PennyFlip.ActivitySourceList;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceSortOrder;

import java.util.List;

public interface ActivitySourceListConsumer {

    @Nullable
    List<Source> querySourceData(final int transactionType);

    boolean queryWithSearch(final CharSequence query);

    void setSourceOrder(@NonNull final SourceSortOrder sortOrder);

    void saveSource(@NonNull final Source source);
}
