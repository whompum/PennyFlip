package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListClickListener;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;
import com.whompum.PennyFlip.ActivitySourceList.Models.SourceMetaData;

import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public abstract class FragmentSourceList extends Fragment implements SourceListClickListener {

    @LayoutRes
    protected static final int LAYOUT = R.layout.layout_source_master_list;

    protected RecyclerView list;
    protected SourceListAdapterBase listAdapter;

    private FloatingActionButton addFab;

    private AnimateScale animator;

    protected Intent intent;

    private IntentReciever intentReciever;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listAdapter = manifestAdapter();
        populate(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            intentReciever = (IntentReciever) context;
        }catch(ClassCastException e){
            Log.i("ActivitySourceList", "The activity ust implement the IntentReciever interface");
            e.printStackTrace();
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(LAYOUT, container, false);

        this.list = view.findViewById(R.id.id_source_master_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter.registerSouceListClickListener(this);

        this.list.setAdapter(listAdapter);

        this.list.addOnScrollListener(scrollListener);

        addFab = view.findViewById(R.id.id_source_master_add_fab);
        animator = new AnimateScale(addFab, true);

    return view;
    }


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                animator.hide(250L);
            else if(newState == RecyclerView.SCROLL_STATE_IDLE)
                animator.show(250L);
        }
    };


    public void onItemSelected(@NonNull SourceMetaData data){
        createIntent(data);

        initIntentArgs(data);

        intentReciever.onDeliverIntent(intent);
    }

    @CallSuper
    protected void initIntentArgs(@NonNull final SourceMetaData data){
        this.intent.putExtra(ActivitySourceData.SOURCE_NAME_KEY, data.getSourceName());
        this.intent.putExtra(ActivitySourceData.SOURCE_TIMESTAMP, data.getLastUpdate());
        this.intent.putExtra(ActivitySourceData.SOURCE_TOTAL, data.getPennies());
    }

    protected void swapDataSet(final List<SourceMetaData> dataList){
        if(listAdapter!=null)
            listAdapter.swapDataset(dataList);
    }

    protected abstract void createIntent(@NonNull SourceMetaData data);
    protected abstract SourceListAdapterBase manifestAdapter();
    protected abstract void populate(CharSequence query);
    protected abstract void filter(Object filter);

}
