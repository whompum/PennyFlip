package com.whompum.PennyFlip.SourceDialog;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 12/21/2017.
 */

public class SourceWrapperAdapter extends RecyclerView.Adapter<SourceWrapperAdapter.SourceViewCache> {

    @LayoutRes
    private static final int LAYOUT = R.layout.layout_list_item_dialog_source;

    @ColorRes
    private static final int TAG_BG_COLOR = R.color.color_light_orange;

    private List<SourceWrapper> wrappers;

    private Context context;

    private int titleColor;
    private int tagBgColor;

    public SourceWrapperAdapter(final Context context, @ColorRes final int textColor){
        this(context, textColor, null);
    }

    public SourceWrapperAdapter(final Context context, @ColorRes final int textColor, final List<SourceWrapper> wrappers){
        this.context = context;

        if(Build.VERSION.SDK_INT >= 23) {
            titleColor = context.getColor(textColor);
            tagBgColor = context.getColor(TAG_BG_COLOR);
        }else {
            titleColor = context.getResources().getColor(textColor);
            tagBgColor = context.getResources().getColor(TAG_BG_COLOR);
        }
        if(wrappers != null)
            this.wrappers = wrappers;
        else
            this.wrappers = new ArrayList<>(1);


    }

    public void insertToFirst(final SourceWrapper eminem){
        wrappers.add(0, eminem);
    }

    public void swapDataSet(@Nullable final List<SourceWrapper> wrappers){

        if(wrappers != null)
         this.wrappers = wrappers;
        else
            this.wrappers = new ArrayList<>(1);

        notifyDataSetChanged();

    }

    @Override
    public SourceViewCache onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SourceViewCache(LayoutInflater.from(context)
                .inflate(LAYOUT, parent, false), titleColor);
    }

    @Override
    public void onBindViewHolder(SourceViewCache holder, int position) {
        final SourceWrapper data = wrappers.get(position);
        holder.wrapperTitle.setText(data.getTitle());

        final SourceWrapper.TAG tag = data.getTagType();
        holder.wrapperTag.setText(data.getTag(tag));

        if(tag == SourceWrapper.TAG.NEW)
            holder.wrapperTag.setBackgroundColor(tagBgColor);

    }

    @Override
    public int getItemCount() {
        return wrappers.size();
    }

    public static class SourceViewCache extends RecyclerView.ViewHolder{

        private ViewGroup layout;
        private TextView  wrapperTitle;
        private TextView  wrapperTag;

        public SourceViewCache(final View layout, final int titleColor){
            super(layout);
            wrapperTitle = layout.findViewById(R.id.id_source_dialog_source_title);
            wrapperTitle.setTextColor(titleColor);
            wrapperTag = layout.findViewById(R.id.id_source_dialog_source_tag);
            this.layout = (ViewGroup) layout;
        }

    }


}
