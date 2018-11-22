package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.DataBind;
import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionHeaderHolder;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionHolder;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Data.ExpansionSnapshotPredicate;
import com.whompum.PennyFlip.Transactions.Data.TransactionsGroupConverter;
import com.whompum.PennyFlip.Transactions.Decoration.TransactionStickyHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        OnItemSelected<Integer>, TransactionStickyHeaders.StickyData, TransactionsGroup.ToggleListener {

    public static final int DATA = 0;
    public static final int HEADER = 1;

    private List<AdapterItem> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    private Timestamp headerDateUtility = Timestamp.now();
    private TransactionsGroup lastBoundHeader;

    private ExpansionSnapshotPredicate expansionPredicate = new ExpansionSnapshotPredicate();
    private HashMap<Long, Boolean> snapshot;

    public TransactionListAdapter(){
        this( new HashMap<Long, Boolean>() );
    }

    public TransactionListAdapter(@NonNull final HashMap<Long, Boolean> snapshot) {
        this.snapshot = snapshot;
        expansionPredicate.setExpansionState( snapshot );
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

        if( inflater == null )
            inflater = LayoutInflater.from( parent.getContext() );

        RecyclerView.ViewHolder holder = null;

        if(viewType == DATA)
            holder = getItemViewHolder( inflater, parent );

        else if(viewType == HEADER)
            holder = getHeaderViewHolder( inflater, parent );

    return holder;
    }

    protected RecyclerView.ViewHolder getItemViewHolder(@NonNull final LayoutInflater inflater,
                                                      @NonNull final ViewGroup parent){
        return new TransactionHolder(
                inflater.inflate( R.layout.transaction_list_item, parent, false )
               );
    }

    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull final LayoutInflater inflater,
                                                      @NonNull final ViewGroup parent){
        return new TransactionHeaderHolder(
                    inflater.inflate(R.layout.transaction_list_dynamic_header, parent, false),
                    this
               );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int viewType = getItemViewType(position);

        final AdapterItem item = dataSet.get(position);

        if( holder.getLayoutPosition() != 0 && holder.itemView.getAlpha() < 1 )
            holder.itemView.setAlpha( 1 );

        if(viewType == DATA)
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
        toggleGroup( pos );
    }

    @Override
    public void onToggle(long millis, boolean isExpanded) {
        setSnapshotState( millis, isExpanded );
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


    public int getLastHeaderItemPos(final int pos){

        if( dataSet != null && dataSet.size() > 0 )
            for( int a = pos; a >= 0; a-- ) //Will return `pos` it is a header
                if( getDataAt( a ) instanceof TransactionsGroup )
                    return a;

        return -1;
    }

    public void swapDataset(@Nullable final List<Transaction> transactions){
        if(transactions == null) return;

        this.dataSet = adapt( transactions );

        for( AdapterItem item: dataSet )
            if( item instanceof TransactionsGroup )
                ((TransactionsGroup) item).setListener( this );

        notifyDataSetChanged();
    }

    private List<AdapterItem> adapt(@NonNull final List<Transaction> transactions){
        return TransactionsGroupConverter.fromTransactions( transactions, expansionPredicate );
    }

    public HashMap<Long, Boolean> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(@NonNull final HashMap<Long, Boolean> snapshot) {
        this.snapshot = snapshot;
    }

    /**
     *
     * @param pos (A-Z) data structure position.
     * @return The previous header that belongs to that position.
     */
    public TransactionsGroup getLastHeader(/* ADAPTER POSITION */final int pos){

        if(pos == 0)
            if((getItemViewType(pos) != HEADER ))
                return null;
            else
                return (TransactionsGroup)dataSet.get(pos);

        for(int i = pos; i >= 0; i--)
            if(getItemViewType(i) == HEADER)
                return (TransactionsGroup) dataSet.get(i);


        return null;
    }


    public void toggleGroup(final int groupPos){

        AdapterItem item;

        if( (item = dataSet.get( groupPos ) ) instanceof TransactionsGroup) {

            final List<TransactionsContent> items = ((TransactionsGroup) item).getChildren();

            final boolean isExpanded = ((TransactionsGroup)item).isExpanded();

            if (!isExpanded) //We're expanding
                expandGroup( groupPos, items );

            if(isExpanded) //collapsing
                collapseGroup( items );

            ((TransactionsGroup)item).toggle(); //Change state

        }

    }

    private void setSnapshotState( final long k, final boolean v ){
        if( snapshot != null )
            snapshot.put( k, v );

    }

    private void expandGroup(final int groupPos, final List<TransactionsContent> items){
        if (dataSet.addAll(groupPos + 1, items))
            notifyItemRangeInserted(groupPos + 1, items.size());
    }

    private void collapseGroup(final List<TransactionsContent> items){
        if (dataSet.removeAll(items))
            notifyDataSetChanged();
    }

    @Override
    public boolean isItemAHeader(int position) {
        if(position <= RecyclerView.NO_POSITION || position >= dataSet.size())
            return false;

        return getItemViewType(position) == TransactionListAdapter.HEADER;
    }

    @Override
    public void bindHeader(View header, final int adapterPos) {

        TransactionsGroup headerItem;

        if ((headerItem = getLastHeader(adapterPos)) == null) return;

        if( lastBoundHeader == headerItem )
            return;
        
        else
            lastBoundHeader = headerItem;

        headerDateUtility.set(System.currentTimeMillis());

        final long now = headerDateUtility.getStartOfDay();

        headerDateUtility.set(headerItem.getMillis());

        final long headerDay = headerDateUtility.getStartOfDay();

        ((TextView) header.findViewById(R.id.id_global_timestamp))
                .setText(((now == headerDay)
                        ? header.getContext().getString(R.string.string_today)
                        : Timestamp.from(headerDay).simpleDate()));

    }

}
