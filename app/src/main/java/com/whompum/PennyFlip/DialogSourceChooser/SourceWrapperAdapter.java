package com.whompum.PennyFlip.DialogSourceChooser;

import android.content.Context;
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


public class SourceWrapperAdapter extends RecyclerView.Adapter<SourceWrapperAdapter.SourceViewCache> {

    private static final int TYPE_NEW = 10;
    private static final int TYPE_REGULAR = 20;

    @LayoutRes
    private static final int LAYOUT = R.layout.dashboard_save_transaction_list_item;

    private AdapterSelecteable<SourceWrapper> wrappers;

    private Context context;
    private int titleColor;

    private OnSourceListItemChange onSourceListItemChange;

    public SourceWrapperAdapter(final Context context, @ColorRes final int textColor){
        this(context, textColor, null);
    }

    public SourceWrapperAdapter(final Context context, @ColorRes final int textColor, final AdapterSelecteable wrappers){
        this.context = context;

        if(Build.VERSION.SDK_INT >= 23)
            titleColor = context.getColor(textColor);

        else
            titleColor = context.getResources().getColor(textColor);

        if(wrappers != null)
            this.wrappers = wrappers;
        else
            this.wrappers = new AdapterSelecteable<>();

    }


    public void insertToFirst(final SourceWrapper wrapper){

        boolean isOldSelected = false;

        /**
         * If the dataset doesn't contain data, skip all logic below and simply add the new one.
         * Else, if the old wrapper is "NEW" then remove it from index zero, and pass it to AdapterSelectable wrappers object
         * to check if that one has been selected. (In this case we are going to tell the client a sourceItem was clicked, but
         * we'll hand it a null value, since that wrapper was clicked but is removed from the list.
         * After this logic is ran, we are going to check IF the new wrapper is UGLY or not.
         * E.G. the user backspaced until the value for SourceWrapper#title is "". In that case we add nothing
         */

        if(wrappers.size() != 0){

            if(wrappers.get(0).getTag() == SourceWrapper.TAG.NEW)
                isOldSelected = wrappers.isSelected(wrappers.remove(0)); //Removing element zero.

            if(isOldSelected)
               notifyListener(null);

            /**
             * If the searched sourcewrapper is actually in the list,
             * then return. NOTE that the order of this logic. First we remove the first index,
             * and only add if its a unique source wrapper
             */
            if(isSearchedSourceInList(wrapper))
                return;

            if(!wrapper.getSourceId().equals(SourceWrapper.FLAG_NON_USABLE)) {
                wrappers.add(0, wrapper);
                Log.i("SourceWrapperAdapter", "insertToFirst()#SourceWrapperAdapter" + " Inserting " +
                " SOURCE NAMED: " + wrappers.get(0).getTag() + " Into the first index");
            }
        }else if( !wrapper.getSourceId().equals(SourceWrapper.FLAG_NON_USABLE))
            wrappers.add(wrapper);


        notifyDataSetChanged();
    }

    private boolean isSearchedSourceInList(final SourceWrapper wrapper){

        for(SourceWrapper theWrapper : this.wrappers)
            if(theWrapper.getSourceId().equals(wrapper.getSourceId()))
                return true;

        return false;
    }


    /**
     * Swaps the data set;
     *
     * This methods responsibility is to set the current data set to the new dataset
     * while also including the original "New" wrapper, if any.
     *
     * @param wrappers The new list of wrappers to swap
     */
    public void swapDataSet(@Nullable final AdapterSelecteable<SourceWrapper> wrappers){

        //We have data
        if(wrappers != null) {

            boolean carryOverFirstIndex = false;  //Whether we should carry over the first index of this.wrappers, and store in the new dataset

            if(this.wrappers.size() != 0)//avoiding an index outta' bounds hissy fit
                //Only carry over if the first index is a new wrapper
                carryOverFirstIndex = this.wrappers.get(0).getTag() == SourceWrapper.TAG.NEW;

            if(carryOverFirstIndex){ //If first index is a new wrapper, cache it, and insert it to the first index
                final SourceWrapper cache = this.wrappers.get(0);
                this.wrappers = wrappers;
                insertToFirst(cache);
            }else
                this.wrappers = wrappers;
        }
        else
            this.wrappers = new AdapterSelecteable<>();

        this.wrappers.removeSelected(); //Dataset changed. Remove any selected items
        notifyListener(null); //Notify a listener that the selected item state has changed

        notifyDataSetChanged();
    }

    protected void notifyListener(@Nullable final SourceWrapper wrapper){

        if(onSourceListItemChange != null)
            onSourceListItemChange.onSourceListItemChange(wrapper);

    }

    public void registerOnClickListener(final OnSourceListItemChange listener){
        this.onSourceListItemChange = listener;
    }


    public void onItemClicked(final int position){

        wrappers.setSelected(wrappers.get(position));

        notifyListener(wrappers.get(position));
        /**
         * Below is used so this class re-calls onBindViewHolder() which turns items on/off based on
         * whether they're in the AdapterSelectable selecteable state; This implementation
         * is extremely bad for preformance, so in the future follow TIP
         * TIP: Use DiffUtil from the RecyclerView.extensions package.
         */
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        final SourceWrapper.TAG tag = wrappers.get(position).getTag();

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
                .inflate(LAYOUT, parent, false), titleColor, SourceWrapper.TAG.NEW, this);

    return holder;
    }

    @Override
    public void onBindViewHolder(SourceViewCache holder, int position) {
        final SourceWrapper data = wrappers.get(position);

        if(wrappers.isSelected(wrappers.get(position)))
            holder.turnOn();
        else
            holder.turnOff();

        holder.wrapperTitle.setText(data.getSourceId());
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

        private SourceWrapperAdapter instance;

        private ViewGroup layout;
        private TextView  wrapperTitle;
        private TextView  wrapperTag;

        public SourceViewCache(final View layout, final int titleColor, final SourceWrapperAdapter instance){
            this(layout, titleColor, SourceWrapper.TAG.REGULAR, instance);
        }

        public SourceViewCache(final View layout, final int titleColor, final SourceWrapper.TAG tag, final SourceWrapperAdapter instance){
            super(layout);

            wrapperTitle = layout.findViewById(R.id.id_source_dialog_source_title);
            wrapperTitle.setTextColor(titleColor);
            wrapperTag = layout.findViewById(R.id.id_source_dialog_source_tag);

            if(tag == SourceWrapper.TAG.REGULAR)
                wrapperTag.setVisibility(View.GONE);

            else if(tag == SourceWrapper.TAG.NEW)
                wrapperTag.setVisibility(View.VISIBLE);



            this.layout = (ViewGroup) layout;
            layout.setOnClickListener(this);

            this.instance = instance;
        }


        @Override
        public void onClick(final View v) {
           instance.onItemClicked(getAdapterPosition());
           setIsRecyclable(false);
        }

        private void turnOn(){
            int color;

            if(Build.VERSION.SDK_INT >=23)
                color = instance.context.getColor(ON_COLOR);
            else
                color = instance.context.getResources().getColor(ON_COLOR);

            layout.setBackgroundColor(color);

        }

        private void turnOff(){
            int color;

            if(Build.VERSION.SDK_INT >=23)
                color = instance.context.getColor(OFF_COLOR);
            else
                color = instance.context.getResources().getColor(OFF_COLOR);

            layout.setBackgroundColor(color);
        }
    }
}
