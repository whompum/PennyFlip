package com.whompum.PennyFlip.Source.SourceListActivity.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Source.SourceListActivity.Models.SourceMetaData;

/**
 * Created by bryan on 12/28/2017.
 */

public interface SourceListClickListener {
    public void onItemSelected(@NonNull final SourceMetaData data);
}
