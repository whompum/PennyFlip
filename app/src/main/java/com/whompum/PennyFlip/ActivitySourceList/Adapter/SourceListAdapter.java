package com.whompum.PennyFlip.ActivitySourceList.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;


public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.Holder> implements AdapterItemClickListener{


    protected int LAYOUT = R.layout.source_list_content_item;

    protected List<Source> dataSet;
    protected LayoutInflater inflater;
    protected OnItemSelected<Source> selectedListener; //Client impl of this interface


    public SourceListAdapter(final Context context){
        this(context, null);
    }

    public SourceListAdapter(final Context context, @Nullable final List<Source> dataSet){
        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
    }

    public void swapDataset(final List<Source> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType){
        return new Holder(inflater.inflate(LAYOUT, parent, false));
    }

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

    @Nullable
    public List<Source> getDataSet(){
        return dataSet;
    }

    private final void registerHolderClickListener(final Holder holder){
        holder.registerItemClickListener(this);
    }

    @Override
    public void listAdapterItemClicked(int listPosition) {
        notifyListener(dataSet.get(listPosition));
    }

    private void notifyListener(final Source data){
        if(selectedListener != null)
            selectedListener.onItemSelected(data);
    }

    public void registerSouceListClickListener(@NonNull OnItemSelected<Source> selectedListener){
        this.selectedListener = selectedListener;
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected AdapterItemClickListener itemClickListener;

        protected TextView sourceName, lastUpdate;
        protected CurrencyEditText statistics;

        public Holder(final View layout){
            super(layout);

            this.sourceName = layout.findViewById(R.id.id_global_title);
            this.lastUpdate = layout.findViewById(R.id.id_global_timestamp);
            this.statistics = layout.findViewById(R.id.id_global_total_display);

            registerClickables( layout );

        }

        private int fetchColor(final int transactionType){

            int color;
            int colorId = ( transactionType == TransactionType.ADD ) ? R.color.dark_green : R.color.dark_red;

            if(Build.VERSION.SDK_INT >= 23)
                color = itemView.getContext().getColor( colorId );
            else
                color = itemView.getContext().getResources().getColor( colorId );

            return color;
        }

        public void bind(final Source data){
            this.sourceName.setText(data.getTitle());
            this.lastUpdate.setText(Timestamp.from(data.getLastUpdate()).getPreferentialDate());
            this.statistics.setText(String.valueOf(data.getPennies()));

            itemView.findViewById( R.id.id_local_veneer ).setBackgroundColor(
                    fetchColor( data.getTransactionType() )
            );

        }

        protected void registerClickables(final View... views){
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
