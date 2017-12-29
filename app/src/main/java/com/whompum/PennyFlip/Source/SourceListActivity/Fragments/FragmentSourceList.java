package com.whompum.PennyFlip.Source.SourceListActivity.Fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Source.SourceListActivity.Adapter.SourceListClickListener;
import com.whompum.PennyFlip.Source.SourceListActivity.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.Source.SourceListActivity.Models.SourceMetaData;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listAdapter = manifestAdapter();
        populate(null);
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
        //Notify activity, and launch new Activity.
    }

    protected void swapDataSet(final List<SourceMetaData> dataList){
        if(listAdapter!=null)
            listAdapter.swapDataset(dataList);
    }

    protected abstract SourceListAdapterBase manifestAdapter();
    protected abstract void populate(CharSequence query);
    protected abstract void filter(Object filter);

}
