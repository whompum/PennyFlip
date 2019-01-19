package com.whompum.PennyFlip.ActivityStatistics.Fragment;

import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.DataBind;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;

public class TopSourcesListAdapter extends RecyclerView.Adapter<TopSourcesListAdapter.SourcesViewHolder> {

    @LayoutRes
    public static final int LAYOUT = R.layout.statistics_source_list_item;

    private List<Source> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    @NonNull
    @Override
    public SourcesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if( inflater == null )
            inflater = LayoutInflater.from( viewGroup.getContext() );

        return new SourcesViewHolder( inflater.inflate( LAYOUT, viewGroup, false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull SourcesViewHolder sourcesViewHolder, int i) {

        sourcesViewHolder.bind( dataSet.get( i ) );

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void swapDataSet(@Size(min = 0, max = 5) @NonNull final List<Source> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public static class SourcesViewHolder extends RecyclerView.ViewHolder implements DataBind<Source> {

        @BindView(R.id.id_global_title) public TextView title;
        @BindView(R.id.id_global_total_display) public CurrencyEditText currencyEditText;

        @BindColor(R.color.dark_green) public int green;
        @BindColor(R.color.dark_red) public int red;

        public SourcesViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind( this, itemView );
        }

        @Override
        public void bind(@NonNull Source item) {
            title.setText( item.getTitle() );
            currencyEditText.setText( String.valueOf( item.getPennies() ) );
            currencyEditText.setTextColor( getColorForType( item.getTransactionType() ) );
        }

        @ColorInt
        private int getColorForType(@IntRange(from = ADD, to = SPEND) final int type){
            return (type == ADD) ? green : red;
        }

    }

}
