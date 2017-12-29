package com.whompum.PennyFlip.Source.SourceListActivity.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Source.SourceListActivity.Models.SourceMetaData;

import java.util.List;

/**
 * Created by bryan on 12/28/2017.
 */

public abstract class SourceListAdapterBase extends RecyclerView.Adapter<SourceListAdapterBase.Holder> implements AdapterItemClickListener{


    protected int LAYOUT = Integer.MIN_VALUE;


    protected List<SourceMetaData> dataSet;
    protected LayoutInflater inflater;
    protected SourceListClickListener selectedListener; //Client impl of this interface


    public SourceListAdapterBase(final Context context){
        this(context, null);
    }

    public SourceListAdapterBase(final Context context, @Nullable final List<SourceMetaData> dataSet){
        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
        this.LAYOUT = setLayout();
    }


    protected abstract int setLayout();

    public void swapDataset(final List<SourceMetaData> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    protected View getLayout(final ViewGroup parent, boolean attatch){
        return inflater.inflate(LAYOUT, parent, attatch);
    }

    @Override
    public abstract Holder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(dataSet.get(position));
        registerHolderClickListener(holder);
    }

    @Override
    public int getItemCount() {
        if(dataSet!=null)
        return dataSet.size();

    return 0;
    }

    private final void registerHolderClickListener(final Holder holder){
        holder.registerItemClickListener(this);
    }

    @Override
    public void listAdapterItemClicked(int listPosition) {
        notifyListener(dataSet.get(listPosition));
    }

    private void notifyListener(final SourceMetaData data){
        if(selectedListener != null)
            selectedListener.onItemSelected(data);
    }

    public void registerSouceListClickListener(@NonNull SourceListClickListener selectedListener){
        this.selectedListener = selectedListener;
    }

    public abstract static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected AdapterItemClickListener itemClickListener;

        public Holder(final View layout){
            super(layout);
        }

        public abstract void bind(final SourceMetaData data);


        protected void registerViewsToClick(final View... views){
            for(View a : views)
                a.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null)
                itemClickListener.listAdapterItemClicked(getAdapterPosition());
        }

        public void registerItemClickListener(@NonNull final AdapterItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

    }

}
