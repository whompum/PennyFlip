package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapter;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;

import java.util.Collection;
import java.util.List;

public class FragmentSourceList extends ListFragment<Source> {

    private IntentReciever intentReciever; //Callback impl to recieve the startActivity intent

    protected SourceListAdapter listAdapter = new SourceListAdapter();

    public static ListFragment<Source> newInstance(){
        return new FragmentSourceList();
    }

    public static ListFragment<Source> newInstance(@NonNull @LayoutRes final Integer noDataResLayout){
        final FragmentSourceList fragmentSourceList = new FragmentSourceList();

        fragmentSourceList.setArguments(
                ListFragment.makeArguments( noDataResLayout )
        );

        return fragmentSourceList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            intentReciever = (IntentReciever) context;
        }catch(ClassCastException e){
            Log.i("ActivitySourceList", "The activity must implement the IntentReciever interface");
            e.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        list.setLayoutManager( new LinearLayoutManager( getContext() ) );

        listAdapter.registerItemClickListener( this );

        list.addItemDecoration( new SourceListMarginDecorator(
                getContext().getResources().getDimensionPixelSize( R.dimen.dimen_padding_ver_base )
        ) );

        list.setAdapter( listAdapter );

        super.onViewCreated( view, savedInstanceState );
    }

    @Override
    protected void handleNewData(@NonNull Collection<Source> data) {
        listAdapter.swapDataset( (List<Source>) data );
    }

    @Override
    public void onItemSelected(Integer integer) {

        List<Source> dataSet;

        if( ( dataSet = listAdapter.getDataSet() ) != null )
            handleItemClick( dataSet.get( integer ) );

    }

    private void handleItemClick(@NonNull final Source source){
        final Intent intent = new Intent( getActivity(), ActivitySourceData.class );
        intent.putExtra( ActivitySourceData.DATA, source );

        intentReciever.onDeliverIntent( intent );
    }

}
