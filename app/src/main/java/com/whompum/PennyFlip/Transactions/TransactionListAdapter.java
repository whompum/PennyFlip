package com.whompum.PennyFlip.Transactions;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Transactions.Models.HeaderItem;
import com.whompum.PennyFlip.Transactions.Models.TransactionHeaderItem;
import com.whompum.PennyFlip.Transactions.Models.TransactionsItem;
import com.whompum.PennyFlip.Transactions.ViewHolder.TransactionHeaderHolder;
import com.whompum.PennyFlip.Transactions.ViewHolder.TransactionHolder;
import com.whompum.PennyFlip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int DATA = 0;
    public static final int HEADER = 1;


    private List<HeaderItem> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    private int addColor;
    private int spendColor;


    public TransactionListAdapter(final Context context){
        this(context, null);
    }

    public TransactionListAdapter(final Context context, final List<HeaderItem> list){

        if(list != null)
            this.dataSet.addAll(list);

        inflater = LayoutInflater.from(context);


        if(Build.VERSION.SDK_INT >= 23){
            addColor = context.getColor(R.color.light_green);
            spendColor = context.getColor(R.color.light_red);
        }else{
            addColor = context.getResources().getColor(R.color.light_green);
            spendColor = context.getResources().getColor(R.color.light_red);
        }


    }

    public HeaderItem getDataAt(final int position){
        if( !(dataSet.size() > position) )
            return null;

        return dataSet.get(position);
    }

    public TransactionHeaderItem getFirstHeader(){

        if(dataSet != null)
            for (int a = 0; a < dataSet.size(); a++)
                if (dataSet.get(a) instanceof TransactionHeaderItem)
                    return (TransactionHeaderItem) dataSet.get(a);

        return null;
    }




    public void swapDataset(final List<HeaderItem> transactions){
        this.dataSet = new ArrayList<>(transactions);
        notifyDataSetChanged();
    }

    /**
     * VERY IMPORTANT NOTICE! the position parameter is a dataset index, not a Recyclerview layout index;
     *
     * @param pos (N-N) data structure position.
     * @return The previous header that belongs to that position.
     */
    public TransactionHeaderItem getLastHeader(final int pos){

        /**
         * If a position zero, then most likely the item is a header. If not
         */

        if(pos == 0){
            HeaderItem item = dataSet.get(pos);
            if((getItemViewType(pos) != HEADER ))
                return null;
            else
                return (TransactionHeaderItem)item;
        }


        for(int i = pos; i >= 0; i--){
            HeaderItem item = dataSet.get(i);

            if(getItemViewType(i) == HEADER)
                return (TransactionHeaderItem) item;
        }


    return null;
    }




    @Override
    public int getItemViewType(int position) {


        final HeaderItem item = dataSet.get(position);

        if(item instanceof TransactionHeaderItem)
            return HEADER;

        else if(item instanceof TransactionsItem)
            return DATA;

        else
            throw new IllegalArgumentException("Wrong HeaderItem Type.");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if(viewType == DATA)
            holder = new TransactionHolder(inflater.inflate(R.layout.history_list_item, parent, false), addColor, spendColor);
        else if(viewType == HEADER)
            holder = new TransactionHeaderHolder(inflater.inflate(R.layout.history_list_item_header, parent, false));

    return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        /**
         * First check the type of Header Item, then check if the Holder is the right type.
         * Then cast and pass.
         */

        final int viewType = getItemViewType(position);

        final HeaderItem item = dataSet.get(position);

        if(viewType ==  DATA)
            ((DataBind<TransactionsItem>)holder).bind( ((TransactionsItem)item) );

        else if(viewType == HEADER)
            ((DataBind<TransactionHeaderItem>)holder).bind((TransactionHeaderItem)item);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public interface DataBind<T>{
        void bind(final T headerItem);
    }

}
