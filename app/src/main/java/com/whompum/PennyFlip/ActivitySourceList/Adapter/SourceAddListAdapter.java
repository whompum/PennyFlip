package com.whompum.PennyFlip.ActivitySourceList.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Sources.SourceMetaData;

import java.util.List;


public class SourceAddListAdapter extends SourceListAdapterBase {

    @ColorRes
    public static final int STATISTICS_COLOR = R.color.light_green;
    private int statsColor;

    public SourceAddListAdapter(final Context context){
        this(context, null);
    }

    public SourceAddListAdapter(final Context context, final List<SourceMetaData> data){
        super(context, data);

        if(Build.VERSION.SDK_INT >= 23)
            statsColor = context.getColor(STATISTICS_COLOR);
        else
            statsColor = context.getResources().getColor(STATISTICS_COLOR);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddListHolderBase(super.getLayout(parent, false), statsColor);
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_source_list_item;
    }


    public static class AddListHolderBase extends SourceListHolderBase {

        public AddListHolderBase(final View layout, final int statsColor){
            super(layout);
            statistics.setTextColor(statsColor);
        }

    }


}
