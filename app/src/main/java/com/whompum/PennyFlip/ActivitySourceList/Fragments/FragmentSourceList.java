package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.FragmentInflationObserver;
import com.whompum.PennyFlip.InflationObserver;
import com.whompum.PennyFlip.InflationOperation;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapter;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentSourceList extends Fragment implements OnItemSelected<Source>, SourceListClientContract {

    public static final String NO_DATA_LAYOUT_KEY = "noDataLayout.ky";

    @LayoutRes
    protected static final int LAYOUT = R.layout.source_list_content;

    @LayoutRes   //Needed to resolve the view to populate error_frame with
    private int noDataResLayout = -1;

    private IntentReciever intentReciever; //Callback impl to recieve the startActivity intent

    protected SourceListAdapter listAdapter = new SourceListAdapter();

    @BindView(R.id.id_global_list) public RecyclerView list;

    @BindView(R.id.id_error_frame) public ViewGroup errorFrame;

    private Unbinder unbinder;

    private InflationObserver inflationObserver = new FragmentInflationObserver();

    public static FragmentSourceList newInstance(){
        return newInstance( -1 );
    }

    public static FragmentSourceList newInstance(@NonNull @LayoutRes final Integer noDataResLayout){
        final FragmentSourceList fragmentSourceList = new FragmentSourceList();

        final Bundle b = new Bundle();
        b.putInt( NO_DATA_LAYOUT_KEY, noDataResLayout );

        fragmentSourceList.setArguments( b );

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();

        if( args != null )
            noDataResLayout = args.getInt( NO_DATA_LAYOUT_KEY,-1 );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(LAYOUT, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        if( noDataResLayout != -1 )
            errorFrame.addView( inflater.inflate( noDataResLayout, errorFrame, false ) );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inflationObserver.onViewInflated();

        list.setLayoutManager( new LinearLayoutManager( getContext(),
                LinearLayoutManager.VERTICAL, false)
        );

        listAdapter.registerSouceListClickListener( this );

        list.addItemDecoration( new SourceListMarginDecorator(
                getContext().getResources().getDimensionPixelSize( R.dimen.dimen_padding_ver_base )
        ) );

        list.setAdapter( listAdapter );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void display(@NonNull List<Source> data) {
        swapAdapterData( data );
    }

    @Override
    public void onNoData() {

        if( getView() != null )  //Is displayed
            toggleErrorFrame( true );

        else inflationObserver.subscribe( new InflationOperation() {
            @Override
            public void onInflated() {
                onNoData();
            }
        } );
    }

    private void swapAdapterData(@NonNull final List<Source> data){

        if( getView() != null ) {

            if( list.getVisibility() != View.VISIBLE )
                toggleErrorFrame( false );

            this.listAdapter.swapDataset(data);
            listAdapter.notifyDataSetChanged();
        }

        else inflationObserver.subscribe(new InflationOperation() {
            @Override
            public void onInflated() {
                swapAdapterData( data );
            }
        } );

    }

    private void toggleErrorFrame(final boolean shouldShow){

        if( shouldShow ){
            errorFrame.setVisibility( View.VISIBLE );
            list.setVisibility( View.GONE );
        } else{
            list.setVisibility( View.VISIBLE );
            errorFrame.setVisibility( View.GONE );
        }
    }

    public void onItemSelected(@NonNull Source data){
        final Intent intent = new Intent(getActivity(), ActivitySourceData.class);
        intent.putExtra(ActivitySourceData.DATA, data);

        intentReciever.onDeliverIntent(intent);
    }

}
