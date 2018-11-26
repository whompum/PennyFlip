package com.whompum.PennyFlip.ListUtils;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.FragmentInflationObserver;
import com.whompum.PennyFlip.InflationObserver;
import com.whompum.PennyFlip.InflationOperation;
import com.whompum.PennyFlip.R;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ListFragment<T> extends Fragment implements CollectionQueryReceiver<T>, OnItemSelected<Integer> {

    public static final String NO_DATA_LAYOUT_KEY = "noDataLayout.ky";

    @LayoutRes
    protected int LAYOUT = R.layout.generic_list_layout;

    @LayoutRes   //Needed to resolve the view to populate error_frame with
    protected int noDataResLayout = R.layout.default_no_data_layout;

    @BindView(R.id.id_global_list) public RecyclerView list;

    @BindView(R.id.id_error_frame) public ViewGroup errorFrame;

    @BindView(R.id.id_global_container) public ViewGroup listContainer;

    protected Unbinder unbinder;

    private InflationObserver inflationObserver = new FragmentInflationObserver();

    protected static Bundle makeArguments(@LayoutRes final int noDataResLayout){
        final Bundle b = new Bundle();
        setNoDataLayoutArg( b, noDataResLayout );

        return b;
    }

    protected static void setNoDataLayoutArg(@NonNull final Bundle b, @LayoutRes final int noDataResLayout){
        b.putInt( NO_DATA_LAYOUT_KEY, noDataResLayout );
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle b = getArguments();

        if( b == null )
            throw new IllegalArgumentException("The bundle mustn't be null. We need it to obtain the error layout");

        else
            noDataResLayout = b.getInt( NO_DATA_LAYOUT_KEY, R.layout.default_no_data_layout );

    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate( LAYOUT, container, false );

        this.unbinder = ButterKnife.bind( this, view );

        initializeUiState( view );

        return view;
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inflationObserver.onViewInflated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(Integer integer) {

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

    @Override
    public void display(@NonNull final Collection<T> data) {

        if( getView() != null ) {

            if( listContainer.getVisibility() != View.VISIBLE )
                toggleErrorFrame( false );

            handleNewData( data );
        }

        else
            inflationObserver.subscribe(new InflationOperation() {
                @Override
                public void onInflated() {
                    handleNewData( data );
                }
            });

    }

    protected abstract void handleNewData(@NonNull final Collection<T> data);

    @CallSuper
    protected void initializeUiState(@NonNull final View layout){
        errorFrame.addView( LayoutInflater.from( getContext() )
                .inflate( noDataResLayout, errorFrame, false ) );
    }

    @CallSuper
    protected void toggleErrorFrame(final boolean shouldShow){

        if( shouldShow ){
            errorFrame.setVisibility( View.VISIBLE );
            listContainer.setVisibility( View.GONE );
        } else{
            listContainer.setVisibility( View.VISIBLE );
            errorFrame.setVisibility( View.GONE );
        }
    }
    

}
