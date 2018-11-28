package com.whompum.PennyFlip.Dashboard.Fragments.Adapter;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.ArrayList;
import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class TodayTransactionAdapter extends RecyclerView.Adapter<TodayTransactionAdapter.TodayHolder> {

    private List<Transaction> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    private int transactionType;

    public TodayTransactionAdapter(int transactionType) {
        this.transactionType = transactionType;
    }

    public void swapDataset(final List<Transaction> data){
        if( data != null )
            this.dataSet = data;

        notifyDataSetChanged();
    }

    @Override
    public TodayHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if( inflater == null )
            inflater = LayoutInflater.from( parent.getContext() );

        return new TodayHolder(
                inflater.inflate( R.layout.dashboard_today_list_item, parent, false ),
                transactionType
        );
    }

    @Override
    public void onBindViewHolder(TodayHolder holder, int position) {
        holder.bind( dataSet.get( position ) );
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class TodayHolder extends RecyclerView.ViewHolder{

        private TextView timeStamp;
        private TextView sourceName;
        private CurrencyEditText transactionAmount;

        public TodayHolder(final View layout, final int transactionType){
            super( layout );

            timeStamp = layout.findViewById( R.id.id_global_timestamp );
            sourceName = layout.findViewById( R.id.id_global_title );
            transactionAmount = layout.findViewById( R.id.id_global_total_display );

            ((CardView)layout.findViewById(R.id.id_local_card_container)).setCardBackgroundColor(
                    resolveColor( transactionType )
            );

        }

        public void bind(final Transaction item){
            timeStamp.setText( Timestamp.from( item.getTimestamp() ).simpleTime() );
            sourceName.setText( item.getSourceId() );
            transactionAmount.setText( String.valueOf( item.getAmount() ) );
        }

        private int resolveColor(final int transactionType){

            int color;
            int colorId = ( transactionType == TransactionType.ADD ) ? R.color.dark_green : R.color.dark_red;

            if(Build.VERSION.SDK_INT >= 23)
                color = itemView.getContext().getColor( colorId );
            else
                color = itemView.getContext().getResources().getColor( colorId );

            return color;
        }

    }

}
