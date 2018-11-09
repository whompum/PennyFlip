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

    @LayoutRes
    protected static final int LAYOUT = R.layout.source_list_content;

    private IntentReciever intentReciever; //Callback impl to recieve the startActivity intent

    protected SourceListAdapter listAdapter = new SourceListAdapter();

    @BindView(R.id.id_global_list) public RecyclerView list;

    private Unbinder unbinder;

    public static FragmentSourceList newInstance(){
        return new FragmentSourceList();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if( container == null ) //Handle edge-cases where fragments are re-used but their views aren't
            getFragmentManager().beginTransaction()
            .remove( this ).commit();

        final View view = inflater.inflate(LAYOUT, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    public void clear() {
        swapAdapterData( new ArrayList<Source>() );
    }

    private void swapAdapterData(@NonNull final List<Source> data){

        Log.i("RESTART_FIX", "swapAdapterData()");

        Log.i("RESTART_FIX", "swapAdapterData() data size: " + data.size() );

        Log.i("RESTART_FIX", "swapAdapterData() is view null: " +
        String.valueOf(getView() == null)
        );


        this.listAdapter.swapDataset( data );
        listAdapter.notifyDataSetChanged();

    }

    public void onItemSelected(@NonNull Source data){
        final Intent intent = new Intent(getActivity(), ActivitySourceData.class);
        intent.putExtra(ActivitySourceData.DATA, data);

        intentReciever.onDeliverIntent(intent);
    }

}
