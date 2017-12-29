package com.whompum.PennyFlip.Source.SourceListActivity.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.PurseTotalView;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Source.SourceListActivity.Models.SpendSourceMetaData;
import com.whompum.PennyFlip.Source.SourceListActivity.Models.SourceMetaData;

import java.util.List;

/**
 * Created by bryan on 12/29/2017.
 */

public class SourceSpendListAdapter extends SourceListAdapterBase {

    @ColorRes
    private static final int STATISTICS_COLOR = R.color.light_red;
    private int statsColor;

    public SourceSpendListAdapter(final Context context){
        this(context, null);
    }

    public SourceSpendListAdapter(final Context context, final List<SourceMetaData> metaDataList){
        super(context, metaDataList);

        if(Build.VERSION.SDK_INT >= 23)
            statsColor = context.getColor(STATISTICS_COLOR);
        else
            statsColor = context.getResources().getColor(STATISTICS_COLOR);
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_source_master_list_item_spend;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpendListHolderBase(getLayout(parent, false), statsColor);
    }

    //Extend AddListHolderBase since most logic i need for the views is in there
    public static class SpendListHolderBase extends SourceListHolderBase {

        private PurseTotalView purseTotalView;

        public SpendListHolderBase(final View layout, final int statsColor){
            super(layout);
            purseTotalView = layout.findViewById(R.id.id_source_master_list_source_purse);
            statistics.setTextColor(statsColor);
        }

        @Override
        public void bind(SourceMetaData data) {
            super.bind(data);

            if( ((SpendSourceMetaData)data).getPurseValue() == SpendSourceMetaData.NO_PURSE )
                purseTotalView.setNAEnabled(true);
            else
            purseTotalView.setValue(((SpendSourceMetaData)data).getPurseValue());
        }
    }


}
