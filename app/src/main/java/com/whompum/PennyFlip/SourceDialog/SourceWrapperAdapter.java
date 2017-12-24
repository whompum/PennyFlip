package com.whompum.PennyFlip.SourceDialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO fix bug where previously tapped item doesn't always turn off when a new element is tapped.
 */

public class SourceWrapperAdapter extends RecyclerView.Adapter<SourceWrapperAdapter.SourceViewCache> {

    private static final int TYPE_NEW = 10;
    private static final int TYPE_REGULAR = 20;

    @LayoutRes
    private static final int LAYOUT = R.layout.layout_list_item_dialog_source;

    @ColorRes
    private static final int TAG_BG_COLOR = R.color.color_light_orange;

    private AdapterSelecteable<SourceWrapper> wrappers;

    private Context context;

    private int titleColor;
    private int tagBgColor;


    private OnSourceListItemCliked onSourceListItemCliked;


    public SourceWrapperAdapter(final Context context, @ColorRes final int textColor){
        this(context, textColor, null);
    }

    public SourceWrapperAdapter(final Context context, @ColorRes final int textColor, final AdapterSelecteable wrappers){
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
            this.wrappers = new AdapterSelecteable<>();


    }



    public void insertToFirst(final SourceWrapper wrapper){

        if(wrappers.size() != 0){
            if(wrappers.get(0).getTagType() == SourceWrapper.TAG.NEW)
                wrappers.remove(0);

            //Adds a NEW wrapper
            if(!wrapper.getTitle().equals(SourceWrapper.EMPTY))
                wrappers.add(0, wrapper);
        }else
            wrappers.add(wrapper);


        notifyDataSetChanged();
    }

    public void swapDataSet(@Nullable final AdapterSelecteable<SourceWrapper> wrappers){

        if(wrappers != null)
            this.wrappers = wrappers;
        else
            this.wrappers = new AdapterSelecteable<>();

        notifyDataSetChanged();

    }

    public void registerOnClickListener(final OnSourceListItemCliked listener){
        this.onSourceListItemCliked = listener;
    }



    public void onItemClicked(final SourceViewCache item, final int position){
        Log.i("test", "ITEM WAS CLICKED");

        wrappers.setSelected(wrappers.get(position));

        if(onSourceListItemCliked!=null)
             onSourceListItemCliked.onSourceItemClicked(wrappers.get(position));
        /**
         * Below is used so this class re-calls onBindViewHolder() which turns items on/off based on
         * whether they're in the AdapterSelectable selecteable state; This implementation
         * is extremely bad for preformance, so in the future follow TIP
         * TIP: Use DiffUtil from the RecyclerView.extensions package.
         */
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(SourceViewCache holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemViewType(int position) {
        final SourceWrapper.TAG tag = wrappers.get(position).getTagType();

        if(tag.equals(SourceWrapper.TAG.NEW))
            return TYPE_NEW;

    return TYPE_REGULAR;
    }

    @Override
    public SourceViewCache onCreateViewHolder(ViewGroup parent, int viewType) {
        SourceViewCache holder = null;

        if(viewType == TYPE_REGULAR)
            holder = new SourceViewCache(LayoutInflater.from(context)
                .inflate(LAYOUT, parent, false), titleColor, this);

        else if(viewType == TYPE_NEW)
            holder = new SourceViewCache(LayoutInflater.from(context)
                .inflate(LAYOUT, parent, false), titleColor, tagBgColor, this);

    return holder;
    }

    @Override
    public void onBindViewHolder(SourceViewCache holder, int position) {
        final SourceWrapper data = wrappers.get(position);

        if(wrappers.isSelected(wrappers.get(position)))
            holder.turnOn();
        else
            holder.turnOff();

        holder.wrapperTitle.setText(data.getTitle());
        holder.wrapperTag.setText(data.getTag());
    }

    @Override
    public int getItemCount() {
        return wrappers.size();
    }


    public static class SourceViewCache extends RecyclerView.ViewHolder implements View.OnClickListener{

        @ColorRes
        private int ON_COLOR = R.color.lightest_grey;
        @ColorRes
        private int OFF_COLOR = R.color.white;

        private SourceWrapperAdapter instance;  //Is this even a smart design ?!

        private ViewGroup layout;
        private TextView  wrapperTitle;
        private TextView  wrapperTag;

        public SourceViewCache(final View layout, final int titleColor, final SourceWrapperAdapter instance){
            this(layout, titleColor, Integer.MIN_VALUE, instance);
        }

        public SourceViewCache(final View layout, final int titleColor, final int tagbgColor, final SourceWrapperAdapter instance){
            super(layout);

            wrapperTitle = layout.findViewById(R.id.id_source_dialog_source_title);
            wrapperTitle.setTextColor(titleColor);
            wrapperTag = layout.findViewById(R.id.id_source_dialog_source_tag);

            if(tagbgColor!=Integer.MIN_VALUE)
                if(Build.VERSION.SDK_INT >= 21)
                wrapperTag.setBackgroundTintList(ColorStateList.valueOf(tagbgColor));
            else
                wrapperTag.setBackgroundColor(tagbgColor);

            this.layout = (ViewGroup) layout;
            layout.setOnClickListener(this);

            this.instance = instance;
        }


        @Override
        public void onClick(final View v) {
           instance.onItemClicked(this, getAdapterPosition());
           setIsRecyclable(false);
        }

        private void turnOn(){
        Log.i("test", "FUCK ME");
            int color = 0;

            if(Build.VERSION.SDK_INT >=23)
                color = instance.context.getColor(ON_COLOR);
            else
                color = instance.context.getResources().getColor(ON_COLOR);

            layout.setBackgroundColor(color);

        }

        private void turnOff(){
            int color = 0;

            if(Build.VERSION.SDK_INT >=23)
                color = instance.context.getColor(OFF_COLOR);
            else
                color = instance.context.getResources().getColor(OFF_COLOR);

            layout.setBackgroundColor(color);
        }



    }


}
