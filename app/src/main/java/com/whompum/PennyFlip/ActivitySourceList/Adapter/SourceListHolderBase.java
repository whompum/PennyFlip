package com.whompum.PennyFlip.ActivitySourceList.Adapter;

import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Source.SourceMetaData;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 12/29/2017.
 */

public class SourceListHolderBase extends SourceListAdapterBase.Holder {

    protected TextView sourceName, lastUpdate, viewButton;
    protected CurrencyEditText statistics;

    public SourceListHolderBase(final View layout){
        super(layout);

        this.sourceName = layout.findViewById(R.id.id_source_master_list_source_label);
        this.lastUpdate = layout.findViewById(R.id.id_source_master_list_source_last_update);
        this.viewButton = layout.findViewById(R.id.id_source_master_list_source_view);
        this.statistics = layout.findViewById(R.id.id_source_master_list_source_statistics);

        registerViewsToClick(viewButton);
    }

    @CallSuper
    @Override
    public void bind(SourceMetaData data) {
        this.sourceName.setText(data.getSourceName());
        this.lastUpdate.setText(data.getLastUpdate());
        this.statistics.setText(String.valueOf(data.getPennies()));
    }
}


