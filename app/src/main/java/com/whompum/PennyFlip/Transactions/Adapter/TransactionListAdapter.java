package com.whompum.PennyFlip.Transactions.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionHeaderHolder;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionHolder;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Header.TransactionStickyHeaders;
import com.whompum.PennyFlip.Transactions.Header.TransactionsGroup;

import java.util.List;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        OnItemSelected<Integer>, TransactionStickyHeaders.StickyData{

    public static final int DATA = 0;
    public static final int HEADER = 1;

    private List<AdapterItem> dataSet;

    private LayoutInflater inflater;

    private int addColor;
    private int spendColor;

    private Ts utility = Ts.now();

    public TransactionListAdapter(final Context context){
        this(context, null);
    }

    public TransactionListAdapter(final Context context, final List<AdapterItem> list){

        if(list != null)
            this.dataSet = list;

        inflater = LayoutInflater.from(context);

        if(Build.VERSION.SDK_INT >= 23){
            addColor = context.getColor(R.color.light_green);
            spendColor = context.getColor(R.color.light_red);
        }else{
            addColor = context.getResources().getColor(R.color.light_green);
            spendColor = context.getResources().getColor(R.color.light_red);
        }


    }

    public AdapterItem getDataAt(final int position){
        if( !(dataSet.size() > position) )
            return null;

        return dataSet.get(position);
    }

    public int getNextHeaderItemPos(final int pos){
        //Return the display position of the next Header compared to the current pos, or NO_POS

        if(dataSet != null && pos < dataSet.size() - 1)
            for(int a = pos+1; a < dataSet.size(); a++)
                if(dataSet.get(a) instanceof TransactionsGroup)
                    return a;

        return -1;
    }

    public void swapDataset(@Nullable final List<AdapterItem> transactions){
          if(transactions == null) return;

        this.dataSet = transactions;
        notifyDataSetChanged();
    }

    /**
     * VERY IMPORTANT NOTICE! the position parameter is a dataset index, not a Recyclerview layout index;
     *
     * @param pos (N-N) data structure position.
     * @return The previous header that belongs to that position.
     */
    public TransactionsGroup getLastHeader(final int pos){

        /**
         * If a position zero, then most likely the item is a header. If not
         */

        if(pos == 0){
            AdapterItem item = dataSet.get(pos);
            if((getItemViewType(pos) != HEADER ))
                return null;
            else
                return (TransactionsGroup) item;
        }


        for(int i = pos; i >= 0; i--){
            AdapterItem item = dataSet.get(i);

            if(getItemViewType(i) == HEADER)
                return (TransactionsGroup) item;
        }


    return null;
    }


    @Override
    public int getItemViewType(int position) {

        final AdapterItem item = dataSet.get(position);

        if(item instanceof TransactionsGroup)
            return HEADER;

        else if(item instanceof TransactionsContent)
            return DATA;

        else
            throw new IllegalArgumentException("Wrong Item Type.");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if(viewType == DATA)
            holder = new TransactionHolder(inflater.inflate(R.layout.transaction_list_item, parent, false), addColor, spendColor);
        else if(viewType == HEADER)
            holder = new TransactionHeaderHolder(inflater.inflate(R.layout.transaction_list_dynamic_header, parent, false)
            ,this);

    return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int viewType = getItemViewType(position);

        final AdapterItem item = dataSet.get(position);

        if(viewType ==  DATA)
            ((DataBind<TransactionsContent>)holder).bind( ((TransactionsContent)item) );

        else if(viewType == HEADER)
            ((DataBind<TransactionsGroup>)holder).bind((TransactionsGroup) item);
    }

    @Override
    public int getItemCount() {
        if(dataSet == null) return 0;

     return dataSet.size();
    }


    @Override
    public void onItemSelected(Integer pos) {

        final AdapterItem item = dataSet.get(pos);

        if(item instanceof TransactionsGroup) {


            //If we're turning ON
            //Cast item to TransactionsGroup
            //fetch its children
            //Insert
            //Notify

            //Else if we're Turning Off

            final List<TransactionsContent> items = ((TransactionsGroup) item).getChildren();

            final boolean isExpanded = ((TransactionsGroup)item).isExpanded();

            if (!isExpanded) //We're expanding
                if (dataSet.addAll(pos + 1, items))
                    notifyItemRangeInserted(pos + 1, items.size());

            if(isExpanded){ //collapsing
                 Log.i("EXPANDING", "estoy aqui.");
                    if (dataSet.removeAll(items)) {
                        Log.i("EXPANDING", "Items removed");
                        notifyDataSetChanged();
                    }
                }

                Log.i("EXPANDING", "TRANSACTIONS GROUP IS EXPANDED: " + ((TransactionsGroup)item).isExpanded());

            ((TransactionsGroup)item).toggle();
        }

    }

    @Override
    public boolean isItemAHeader(int position) {
        if(position == RecyclerView.NO_POSITION)
            return false;

        return getItemViewType(position) == TransactionListAdapter.HEADER;
    }

    @Override
    public void bindHeader(View header, final int adapterPos) {
        final TransactionsGroup headerItem = getLastHeader(adapterPos);

        utility.set(System.currentTimeMillis());

        if(headerItem == null) return;

        final long now = utility.getStartOfDay();

        utility.set(headerItem.getMillis());

        final long headerDay = utility.getStartOfDay();

        ((TextView)header.findViewById(R.id.id_global_timestamp))
                .setText(  ((now == headerDay)
                        ? header.getContext().getString(R.string.string_today)
                        : Ts.from(headerDay).simpleDate()) );

    }

    public interface DataBind<T>{
        void bind(final T headerItem);
    }

}
